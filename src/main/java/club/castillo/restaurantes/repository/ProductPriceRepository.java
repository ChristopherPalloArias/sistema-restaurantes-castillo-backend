package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}
