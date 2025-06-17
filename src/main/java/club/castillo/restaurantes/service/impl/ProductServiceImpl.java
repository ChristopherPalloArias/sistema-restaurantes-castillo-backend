package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.ProductRequestDTO;
import club.castillo.restaurantes.dto.ProductResponseDTO;
import club.castillo.restaurantes.dto.CategoryResponseDTO;
import club.castillo.restaurantes.model.*;
import club.castillo.restaurantes.repository.*;
import club.castillo.restaurantes.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductAccompanimentRepository productAccompanimentRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        // Validar que la categoría existe
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        // Validar que no exista otro producto con el mismo nombre en la misma categoría
        if (productRepository.existsByNameAndCategoryId(productRequestDTO.getName(), category.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre en la categoría");
        }

        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .imageUrl(productRequestDTO.getImageUrl())
                .basePrice(productRequestDTO.getBasePrice())
                .category(category)
                .isCustomizable(productRequestDTO.getIsCustomizable() != null ? productRequestDTO.getIsCustomizable() : false)
                .available(true)
                .active(true)
                .build();

        Product savedProduct = productRepository.save(product);

        // Crear tamaños si se proporcionaron
        if (productRequestDTO.getSizes() != null) {
            productRequestDTO.getSizes().forEach(sizeDTO -> 
                addProductSize(savedProduct.getId(), sizeDTO));
        }

        // Crear acompañamientos si se proporcionaron
        if (productRequestDTO.getAccompaniments() != null) {
            productRequestDTO.getAccompaniments().forEach(accompDTO -> 
                addProductAccompaniment(savedProduct.getId(), accompDTO));
        }

        return mapToResponseDTO(productRepository.findById(savedProduct.getId()).orElseThrow());
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        if (!product.getName().equals(productRequestDTO.getName()) &&
                productRepository.existsByNameAndCategoryId(productRequestDTO.getName(), category.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre en la categoría");
        }

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setImageUrl(productRequestDTO.getImageUrl());
        product.setBasePrice(productRequestDTO.getBasePrice());
        product.setCategory(category);
        if (productRequestDTO.getIsCustomizable() != null) {
            product.setIsCustomizable(productRequestDTO.getIsCustomizable());
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void disableProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void enableProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        product.setActive(true);
        productRepository.save(product);
    }

    @Override
    public void setProductAvailability(Long id, boolean available) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        product.setAvailable(available);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        return mapToResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllActiveProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getActiveProductsByCategory(Long categoryId) {
        return productRepository.findActiveByCategoryId(categoryId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getActiveProductsByRestaurant(Long restaurantId) {
        return productRepository.findActiveByRestaurantId(restaurantId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void setProductPrice(Long productId, Long restaurantId, BigDecimal price) {
        setProductPrice(productId, restaurantId, price, LocalDateTime.now(), null);
    }

    @Override
    public void setProductPrice(Long productId, Long restaurantId, BigDecimal price,
                              LocalDateTime validFrom, LocalDateTime validUntil) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));

        // Desactivar precios anteriores que se superpongan con el nuevo período
        List<ProductPrice> existingPrices = productPriceRepository
                .findByProductIdAndRestaurantIdAndActiveTrue(productId, restaurantId);
        
        existingPrices.forEach(existingPrice -> {
            if (validUntil == null || existingPrice.getValidUntil() == null ||
                    (validFrom.isBefore(existingPrice.getValidUntil()) &&
                            (validUntil == null || validUntil.isAfter(existingPrice.getValidFrom())))) {
                existingPrice.setActive(false);
                productPriceRepository.save(existingPrice);
            }
        });

        ProductPrice newPrice = ProductPrice.builder()
                .product(product)
                .restaurant(restaurant)
                .price(price)
                .validFrom(validFrom)
                .validUntil(validUntil)
                .active(true)
                .build();

        productPriceRepository.save(newPrice);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCurrentPrice(Long productId, Long restaurantId) {
        return productPriceRepository.findCurrentPrice(productId, restaurantId, LocalDateTime.now())
                .map(ProductPrice::getPrice)
                .orElseGet(() -> {
                    // Si no hay precio específico, devolver el precio base
                    return productRepository.findById(productId)
                            .map(Product::getBasePrice)
                            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
                });
    }

    @Override
    public void addProductSize(Long productId, ProductRequestDTO.ProductSizeDTO sizeDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (productSizeRepository.existsByNameAndProductId(sizeDTO.getName(), productId)) {
            throw new IllegalArgumentException("Ya existe un tamaño con ese nombre para este producto");
        }

        ProductSize size = ProductSize.builder()
                .product(product)
                .name(sizeDTO.getName())
                .description(sizeDTO.getDescription())
                .priceModifier(sizeDTO.getPriceModifier())
                .active(true)
                .build();

        productSizeRepository.save(size);
    }

    @Override
    public void updateProductSize(Long productId, Long sizeId, ProductRequestDTO.ProductSizeDTO sizeDTO) {
        ProductSize size = productSizeRepository.findById(sizeId)
                .orElseThrow(() -> new EntityNotFoundException("Tamaño no encontrado"));

        if (!size.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("El tamaño no pertenece al producto especificado");
        }

        if (!size.getName().equals(sizeDTO.getName()) &&
                productSizeRepository.existsByNameAndProductId(sizeDTO.getName(), productId)) {
            throw new IllegalArgumentException("Ya existe un tamaño con ese nombre para este producto");
        }

        size.setName(sizeDTO.getName());
        size.setDescription(sizeDTO.getDescription());
        size.setPriceModifier(sizeDTO.getPriceModifier());

        productSizeRepository.save(size);
    }

    @Override
    public void removeProductSize(Long productId, Long sizeId) {
        ProductSize size = productSizeRepository.findById(sizeId)
                .orElseThrow(() -> new EntityNotFoundException("Tamaño no encontrado"));

        if (!size.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("El tamaño no pertenece al producto especificado");
        }

        size.setActive(false);
        productSizeRepository.save(size);
    }

    @Override
    public void addProductAccompaniment(Long productId, ProductRequestDTO.ProductAccompanimentDTO accompDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (productAccompanimentRepository.existsByNameAndProductId(accompDTO.getName(), productId)) {
            throw new IllegalArgumentException("Ya existe un acompañamiento con ese nombre para este producto");
        }

        ProductAccompaniment accompaniment = ProductAccompaniment.builder()
                .product(product)
                .name(accompDTO.getName())
                .description(accompDTO.getDescription())
                .priceModifier(accompDTO.getPriceModifier())
                .isDefault(accompDTO.getIsDefault())
                .maxQuantity(accompDTO.getMaxQuantity())
                .active(true)
                .build();

        productAccompanimentRepository.save(accompaniment);
    }

    @Override
    public void updateProductAccompaniment(Long productId, Long accompanimentId,
                                         ProductRequestDTO.ProductAccompanimentDTO accompDTO) {
        ProductAccompaniment accompaniment = productAccompanimentRepository.findById(accompanimentId)
                .orElseThrow(() -> new EntityNotFoundException("Acompañamiento no encontrado"));

        if (!accompaniment.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("El acompañamiento no pertenece al producto especificado");
        }

        if (!accompaniment.getName().equals(accompDTO.getName()) &&
                productAccompanimentRepository.existsByNameAndProductId(accompDTO.getName(), productId)) {
            throw new IllegalArgumentException("Ya existe un acompañamiento con ese nombre para este producto");
        }

        accompaniment.setName(accompDTO.getName());
        accompaniment.setDescription(accompDTO.getDescription());
        accompaniment.setPriceModifier(accompDTO.getPriceModifier());
        accompaniment.setIsDefault(accompDTO.getIsDefault());
        accompaniment.setMaxQuantity(accompDTO.getMaxQuantity());

        productAccompanimentRepository.save(accompaniment);
    }

    @Override
    public void removeProductAccompaniment(Long productId, Long accompanimentId) {
        ProductAccompaniment accompaniment = productAccompanimentRepository.findById(accompanimentId)
                .orElseThrow(() -> new EntityNotFoundException("Acompañamiento no encontrado"));

        if (!accompaniment.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("El acompañamiento no pertenece al producto especificado");
        }

        accompaniment.setActive(false);
        productAccompanimentRepository.save(accompaniment);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        // Obtener los tamaños activos
        List<ProductResponseDTO.ProductSizeDTO> sizes = productSizeRepository
                .findByProductIdAndActiveTrue(product.getId())
                .stream()
                .map(this::mapToSizeDTO)
                .collect(Collectors.toList());

        // Obtener los acompañamientos activos
        List<ProductResponseDTO.ProductAccompanimentDTO> accompaniments = productAccompanimentRepository
                .findByProductIdAndActiveTrue(product.getId())
                .stream()
                .map(this::mapToAccompanimentDTO)
                .collect(Collectors.toList());

        // Obtener los precios actuales por restaurante
        Map<Long, BigDecimal> restaurantPrices = productPriceRepository
                .findCurrentPricesForProduct(product.getId(), LocalDateTime.now())
                .stream()
                .collect(Collectors.toMap(
                        price -> price.getRestaurant().getId(),
                        ProductPrice::getPrice
                ));

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .basePrice(product.getBasePrice())
                .category(mapToCategoryDTO(product.getCategory()))
                .isCustomizable(product.getIsCustomizable())
                .available(product.getAvailable())
                .active(product.getActive())
                .availableSizes(sizes)
                .availableAccompaniments(accompaniments)
                .restaurantPrices(restaurantPrices)
                .build();
    }

    private ProductResponseDTO.ProductSizeDTO mapToSizeDTO(ProductSize size) {
        return ProductResponseDTO.ProductSizeDTO.builder()
                .id(size.getId())
                .name(size.getName())
                .description(size.getDescription())
                .priceModifier(size.getPriceModifier())
                .active(size.getActive())
                .build();
    }

    private ProductResponseDTO.ProductAccompanimentDTO mapToAccompanimentDTO(ProductAccompaniment accompaniment) {
        return ProductResponseDTO.ProductAccompanimentDTO.builder()
                .id(accompaniment.getId())
                .name(accompaniment.getName())
                .description(accompaniment.getDescription())
                .priceModifier(accompaniment.getPriceModifier())
                .isDefault(accompaniment.getIsDefault())
                .maxQuantity(accompaniment.getMaxQuantity())
                .active(accompaniment.getActive())
                .build();
    }

    private CategoryResponseDTO mapToCategoryDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .active(category.getActive())
                .build();
    }
}

