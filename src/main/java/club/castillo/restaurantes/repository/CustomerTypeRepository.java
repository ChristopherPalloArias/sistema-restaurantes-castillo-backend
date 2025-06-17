package club.castillo.restaurantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import club.castillo.restaurantes.model.CustomerType;


public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
}
