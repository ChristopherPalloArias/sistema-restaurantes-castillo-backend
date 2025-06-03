package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Category;
import club.castillo.restaurantes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByAvailableTrue();
}
