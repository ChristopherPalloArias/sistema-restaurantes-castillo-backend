package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    Optional<MenuProduct> findByMenuIdAndProductId(Long menuId, Long productId);
}
