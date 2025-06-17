package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByIdentification(String identification);
    boolean existsByIdentification(String identification);
    List<Customer> findByActiveTrue();
}