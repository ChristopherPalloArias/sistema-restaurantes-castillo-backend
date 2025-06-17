package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.ProductAccompaniment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAccompanimentRepository extends JpaRepository<ProductAccompaniment, Long> {
    List<ProductAccompaniment> findByProductIdAndActiveTrue(Long productId);
    boolean existsByNameAndProductId(String name, Long productId);
} 