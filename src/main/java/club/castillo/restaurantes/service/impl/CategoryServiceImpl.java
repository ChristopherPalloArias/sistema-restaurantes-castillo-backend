package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.CategoryRequestDTO;
import club.castillo.restaurantes.dto.CategoryResponseDTO;
import club.castillo.restaurantes.model.Category;
import club.castillo.restaurantes.model.Restaurant;
import club.castillo.restaurantes.repository.CategoryRepository;
import club.castillo.restaurantes.repository.RestaurantRepository;
import club.castillo.restaurantes.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRepository.existsByName(categoryRequestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        Category.CategoryBuilder categoryBuilder = Category.builder()
                .name(categoryRequestDTO.getName())
                .description(categoryRequestDTO.getDescription())
                .imageUrl(categoryRequestDTO.getImageUrl())
                .categoryType(categoryRequestDTO.getCategoryType())
                .active(true);

        // Si es específica de un restaurante, validar y asignar el restaurante
        if ("RESTAURANT_SPECIFIC".equals(categoryRequestDTO.getCategoryType())) {
            if (categoryRequestDTO.getRestaurantId() == null) {
                throw new IllegalArgumentException("El ID del restaurante es requerido para categorías específicas de restaurante");
            }
            Restaurant restaurant = restaurantRepository.findById(categoryRequestDTO.getRestaurantId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));
            categoryBuilder.restaurant(restaurant);
        }

        Category savedCategory = categoryRepository.save(categoryBuilder.build());
        return mapToResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        if (!category.getName().equals(categoryRequestDTO.getName()) &&
                categoryRepository.existsByName(categoryRequestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        // No permitir cambiar el tipo de categoría
        if (!category.getCategoryType().equals(categoryRequestDTO.getCategoryType())) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de categoría");
        }

        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        category.setImageUrl(categoryRequestDTO.getImageUrl());

        // Si es específica de restaurante, validar y actualizar el restaurante
        if ("RESTAURANT_SPECIFIC".equals(category.getCategoryType())) {
            if (!category.getRestaurant().getId().equals(categoryRequestDTO.getRestaurantId())) {
                throw new IllegalArgumentException("No se puede cambiar el restaurante de una categoría");
            }
        }

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDTO(updatedCategory);
    }

    @Override
    public void disableById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    public void enableById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        category.setActive(true);
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        return mapToResponseDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        return mapToResponseDTO(category);
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        CategoryResponseDTO.RestaurantDTO restaurantDTO = null;
        if (category.getRestaurant() != null) {
            restaurantDTO = CategoryResponseDTO.RestaurantDTO.builder()
                    .id(category.getRestaurant().getId())
                    .name(category.getRestaurant().getName())
                    .build();
        }

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .categoryType(category.getCategoryType())
                .restaurant(restaurantDTO)
                .active(category.getActive())
                .build();
    }
} 