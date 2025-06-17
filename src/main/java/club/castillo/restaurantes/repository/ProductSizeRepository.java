package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    List<ProductSize> findByProductIdAndActiveTrue(Long productId);
    boolean existsByNameAndProductId(String name, Long productId);
} 