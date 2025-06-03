package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Customer;
import club.castillo.restaurantes.model.Order;
import club.castillo.restaurantes.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByRestaurant(Restaurant restaurant);
}
