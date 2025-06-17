package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.CategoryRequestDTO;
import club.castillo.restaurantes.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);
    void disableById(Long id); // soft delete
    void enableById(Long id);
    CategoryResponseDTO getCategoryById(Long id);
    List<CategoryResponseDTO> getAllCategories();
    List<CategoryResponseDTO> getAllActiveCategories();
    CategoryResponseDTO getCategoryByName(String name);
} 