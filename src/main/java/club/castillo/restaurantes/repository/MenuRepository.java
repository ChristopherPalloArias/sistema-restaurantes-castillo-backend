package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Menu;
import club.castillo.restaurantes.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant(Restaurant restaurant);
}
