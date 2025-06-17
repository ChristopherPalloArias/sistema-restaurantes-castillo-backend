package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.ProductRequestDTO;
import club.castillo.restaurantes.dto.ProductResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {
    // Operaciones básicas de productos
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void disableProduct(Long id);
    void enableProduct(Long id);
    void setProductAvailability(Long id, boolean available);
    
    // Consultas de productos
    ProductResponseDTO getProductById(Long id);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getAllActiveProducts();
    List<ProductResponseDTO> getActiveProductsByCategory(Long categoryId);
    List<ProductResponseDTO> getActiveProductsByRestaurant(Long restaurantId);
    
    // Gestión de precios por restaurante
    void setProductPrice(Long productId, Long restaurantId, BigDecimal price);
    void setProductPrice(Long productId, Long restaurantId, BigDecimal price, 
                        LocalDateTime validFrom, LocalDateTime validUntil);
    BigDecimal getCurrentPrice(Long productId, Long restaurantId);
    
    // Gestión de tamaños
    void addProductSize(Long productId, ProductRequestDTO.ProductSizeDTO sizeDTO);
    void updateProductSize(Long productId, Long sizeId, ProductRequestDTO.ProductSizeDTO sizeDTO);
    void removeProductSize(Long productId, Long sizeId);
    
    // Gestión de acompañamientos
    void addProductAccompaniment(Long productId, ProductRequestDTO.ProductAccompanimentDTO accompanimentDTO);
    void updateProductAccompaniment(Long productId, Long accompanimentId, 
                                  ProductRequestDTO.ProductAccompanimentDTO accompanimentDTO);
    void removeProductAccompaniment(Long productId, Long accompanimentId);
}