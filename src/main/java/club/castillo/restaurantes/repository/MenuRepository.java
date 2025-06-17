package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Menu;
import club.castillo.restaurantes.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant(Restaurant restaurant);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.products mp LEFT JOIN FETCH mp.product WHERE m.id = :id")
    Optional<Menu> findByIdWithProducts(@Param("id") Long id);
}
