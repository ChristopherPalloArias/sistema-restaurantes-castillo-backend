package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
