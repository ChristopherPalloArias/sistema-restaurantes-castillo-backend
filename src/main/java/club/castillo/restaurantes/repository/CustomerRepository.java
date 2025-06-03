package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Customer;
import club.castillo.restaurantes.model.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCustomerType(CustomerType customerType);
    List<Customer> findByStatus(String status);
}
