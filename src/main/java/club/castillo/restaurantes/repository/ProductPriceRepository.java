package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    @Query("SELECT pp FROM ProductPrice pp WHERE pp.product.id = :productId AND pp.restaurant.id = :restaurantId " +
           "AND pp.active = true AND (pp.validUntil IS NULL OR pp.validUntil > :now) " +
           "AND pp.validFrom <= :now ORDER BY pp.validFrom DESC")
    Optional<ProductPrice> findCurrentPrice(@Param("productId") Long productId,
                                          @Param("restaurantId") Long restaurantId,
                                          @Param("now") LocalDateTime now);

    List<ProductPrice> findByProductIdAndRestaurantIdAndActiveTrue(Long productId, Long restaurantId);
    
    @Query("SELECT pp FROM ProductPrice pp WHERE pp.product.id = :productId AND pp.active = true " +
           "AND (pp.validUntil IS NULL OR pp.validUntil > :now) AND pp.validFrom <= :now")
    List<ProductPrice> findCurrentPricesForProduct(@Param("productId") Long productId,
                                                 @Param("now") LocalDateTime now);
}
