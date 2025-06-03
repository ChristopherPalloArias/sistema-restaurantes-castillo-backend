package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Customer;
import club.castillo.restaurantes.model.Order;
import club.castillo.restaurantes.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByRestaurant(Restaurant restaurant);
    List<Order> findByStatus(String status);
}
