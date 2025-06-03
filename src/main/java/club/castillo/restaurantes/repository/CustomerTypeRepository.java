package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
}
