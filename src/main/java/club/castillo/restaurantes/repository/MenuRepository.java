package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Menu;
import club.castillo.restaurantes.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant(Restaurant restaurant);
}
