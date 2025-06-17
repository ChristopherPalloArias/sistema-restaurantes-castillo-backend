package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Restaurant;
import club.castillo.restaurantes.model.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByStatus(RestaurantStatus status);
    List<Restaurant> findByActiveTrue();
}