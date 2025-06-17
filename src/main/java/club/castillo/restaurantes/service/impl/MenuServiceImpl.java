package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.MenuResponseDTO;
import club.castillo.restaurantes.dto.MenuRequestDTO;
import club.castillo.restaurantes.dto.MenuAdminResponseDTO;
import club.castillo.restaurantes.model.*;
import club.castillo.restaurantes.repository.*;
import club.castillo.restaurantes.service.MenuService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductAccompanimentRepository productAccompanimentRepository;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    @Override
    public MenuResponseDTO getRestaurantMenu(Long restaurantId,
                                           boolean showUnavailable,
                                           Optional<Double> maxPrice,
                                           Optional<String> searchTerm) {
        // Verificar que el restaurante existe
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));

        // Obtener todas las categorías activas
        List<Category> categories = categoryRepository.findByActiveTrue();

        // Obtener los productos activos por restaurante
        List<Product> allProducts = productRepository.findActiveByRestaurantId(restaurantId);

        // Filtrar productos según los parámetros
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> showUnavailable || product.getAvailable())
                .filter(product -> !maxPrice.isPresent() || 
                        getCurrentPrice(product.getId(), restaurantId)
                                .compareTo(BigDecimal.valueOf(maxPrice.get())) <= 0)
                .filter(product -> !searchTerm.isPresent() || 
                        matchesSearchTerm(product, searchTerm.get()))
                .collect(Collectors.toList());

        // Agrupar productos por categoría
        Map<Long, List<Product>> productsByCategory = filteredProducts.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getId()));

        // Construir el menú
        List<MenuResponseDTO.MenuCategoryDTO> menuCategories = categories.stream()
                .map(category -> buildMenuCategory(category, 
                        productsByCategory.getOrDefault(category.getId(), Collections.emptyList()),
                        restaurantId))
                .filter(category -> !category.getProducts().isEmpty())
                .collect(Collectors.toList());

        return MenuResponseDTO.builder()
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getName())
                .categories(menuCategories)
                .build();
    }

    private MenuResponseDTO.MenuCategoryDTO buildMenuCategory(Category category,
                                                            List<Product> products,
                                                            Long restaurantId) {
        List<MenuResponseDTO.MenuProductDTO> menuProducts = products.stream()
                .map(product -> buildMenuProduct(product, restaurantId))
                .collect(Collectors.toList());

        return MenuResponseDTO.MenuCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .products(menuProducts)
                .build();
    }

    private MenuResponseDTO.MenuProductDTO buildMenuProduct(Product product, Long restaurantId) {
        // Obtener tamaños activos
        List<MenuResponseDTO.MenuProductSizeDTO> sizes = productSizeRepository
                .findByProductIdAndActiveTrue(product.getId())
                .stream()
                .map(this::buildMenuProductSize)
                .collect(Collectors.toList());

        // Obtener acompañamientos activos
        List<MenuResponseDTO.MenuProductAccompanimentDTO> accompaniments = productAccompanimentRepository
                .findByProductIdAndActiveTrue(product.getId())
                .stream()
                .map(this::buildMenuProductAccompaniment)
                .collect(Collectors.toList());

        return MenuResponseDTO.MenuProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .currentPrice(getCurrentPrice(product.getId(), restaurantId).doubleValue())
                .available(product.getAvailable())
                .isCustomizable(product.getIsCustomizable())
                .availableSizes(sizes)
                .availableAccompaniments(accompaniments)
                .build();
    }

    private MenuResponseDTO.MenuProductSizeDTO buildMenuProductSize(ProductSize size) {
        return MenuResponseDTO.MenuProductSizeDTO.builder()
                .id(size.getId())
                .name(size.getName())
                .description(size.getDescription())
                .priceModifier(size.getPriceModifier().doubleValue())
                .build();
    }

    private MenuResponseDTO.MenuProductAccompanimentDTO buildMenuProductAccompaniment(
            ProductAccompaniment accompaniment) {
        return MenuResponseDTO.MenuProductAccompanimentDTO.builder()
                .id(accompaniment.getId())
                .name(accompaniment.getName())
                .description(accompaniment.getDescription())
                .priceModifier(accompaniment.getPriceModifier().doubleValue())
                .isDefault(accompaniment.getIsDefault())
                .maxQuantity(accompaniment.getMaxQuantity())
                .build();
    }

    private BigDecimal getCurrentPrice(Long productId, Long restaurantId) {
        return productPriceRepository.findCurrentPrice(productId, restaurantId, LocalDateTime.now())
                .map(ProductPrice::getPrice)
                .orElseGet(() -> productRepository.findById(productId)
                        .map(Product::getBasePrice)
                        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado")));
    }

    private boolean matchesSearchTerm(Product product, String searchTerm) {
        String termLower = searchTerm.toLowerCase();
        return product.getName().toLowerCase().contains(termLower) ||
                (product.getDescription() != null &&
                 product.getDescription().toLowerCase().contains(termLower));
    }

    // ----- CRUD de menús -----

    @Override
    @Transactional(readOnly = false)
    public MenuAdminResponseDTO createMenu(MenuRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));

        Menu menu = Menu.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        Menu saved = menuRepository.save(menu);
        return mapToAdminResponse(saved);
    }

    @Override
    @Transactional(readOnly = false)
    public MenuAdminResponseDTO updateMenu(Long id, MenuRequestDTO requestDTO) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menú no encontrado"));

        if (!menu.getRestaurant().getId().equals(requestDTO.getRestaurantId())) {
            Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
            menu.setRestaurant(restaurant);
        }

        menu.setName(requestDTO.getName());
        menu.setDescription(requestDTO.getDescription());

        Menu updated = menuRepository.save(menu);
        return mapToAdminResponse(updated);
    }

    @Override
    @Transactional(readOnly = false)
    public void assignProduct(Long menuId, Long productId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menú no encontrado"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        MenuProduct relation = menuProductRepository.findByMenuIdAndProductId(menuId, productId)
                .orElse(MenuProduct.builder()
                        .menu(menu)
                        .product(product)
                        .build());

        relation.setActive(true);
        menuProductRepository.save(relation);
    }

    @Override
    @Transactional(readOnly = false)
    public void unassignProduct(Long menuId, Long productId) {
        MenuProduct relation = menuProductRepository.findByMenuIdAndProductId(menuId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Relación menú-producto no encontrada"));
        relation.setActive(false);
        menuProductRepository.save(relation);
    }

    private MenuAdminResponseDTO mapToAdminResponse(Menu menu) {
        MenuAdminResponseDTO.RestaurantDTO restaurantDTO = null;
        if (menu.getRestaurant() != null) {
            restaurantDTO = MenuAdminResponseDTO.RestaurantDTO.builder()
                    .id(menu.getRestaurant().getId())
                    .name(menu.getRestaurant().getName())
                    .build();
        }

        List<MenuAdminResponseDTO.ProductDTO> products = Optional.ofNullable(menu.getProducts())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(MenuProduct::getActive)
                .map(mp -> MenuAdminResponseDTO.ProductDTO.builder()
                        .id(mp.getProduct().getId())
                        .name(mp.getProduct().getName())
                        .build())
                .collect(Collectors.toList());

        return MenuAdminResponseDTO.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .active(menu.getActive())
                .restaurant(restaurantDTO)
                .products(products)
                .build();
    }
}
