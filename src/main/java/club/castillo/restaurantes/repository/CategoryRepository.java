package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
