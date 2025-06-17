package club.castillo.restaurantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import club.castillo.restaurantes.model.CustomerType;

import java.util.List;
import java.util.Optional;


public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    Optional<CustomerType> findByName(String name);
    boolean existsByName(String name);
    List<CustomerType> findByActiveTrue();
}
