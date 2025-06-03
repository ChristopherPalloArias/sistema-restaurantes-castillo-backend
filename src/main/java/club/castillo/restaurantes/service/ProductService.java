package club.castillo.restaurantes.service;

import club.castillo.restaurantes.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product save(Product product);
    List<Product> findAll();
    List<Product> findAvailable();
    List<Product> findAllActive();
    Optional<Product> findById(Long id);
    void disableById(Long id); // soft delete
}