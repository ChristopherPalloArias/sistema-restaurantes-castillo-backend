package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Category;
import club.castillo.restaurantes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByActiveTrueAndAvailableTrue();
    List<Product> findByCategoryAndActiveTrue(Category category);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category.id = :categoryId")
    List<Product> findActiveByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category.restaurant.id = :restaurantId")
    List<Product> findActiveByRestaurantId(@Param("restaurantId") Long restaurantId);
    
    boolean existsByNameAndCategoryId(String name, Long categoryId);
}
