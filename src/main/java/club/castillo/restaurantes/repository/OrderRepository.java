package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Customer;
import club.castillo.restaurantes.model.Order;
import club.castillo.restaurantes.model.Restaurant;
import club.castillo.restaurantes.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurant(Restaurant restaurant);
    List<Order> findByStatus(String status);
    
    List<Order> findByRestaurantId(Long restaurantId);
    
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);
    
    List<Order> findByRestaurantIdAndStatusAndCreatedAtBetween(
            Long restaurantId, 
            OrderStatus status, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
    
    List<Order> findByRestaurantIdAndCreatedAtBetween(
            Long restaurantId, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
    
    List<Order> findByUserId(Long userId);
    
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status NOT IN ('DELIVERED', 'CANCELLED') " +
           "ORDER BY o.createdAt DESC")
    List<Order> findActiveByRestaurantId(@Param("restaurantId") Long restaurantId);
}
