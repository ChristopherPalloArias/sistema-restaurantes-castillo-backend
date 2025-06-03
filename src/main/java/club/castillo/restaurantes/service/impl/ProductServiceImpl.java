package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.model.Product;
import club.castillo.restaurantes.repository.ProductRepository;
import club.castillo.restaurantes.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAvailable() {
        return productRepository.findByAvailableTrue();
    }

    @Override
    public List<Product> findAllActive() {
        return productRepository.findByActiveTrue();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void disableById(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setActive(false);
            productRepository.save(product);
        });
    }
}

