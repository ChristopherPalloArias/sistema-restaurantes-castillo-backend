package club.castillo.restaurantes.service;

import club.castillo.restaurantes.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll(); // incluye activos e inactivos
    List<User> findAllActive(); // solo activos
    void disableById(Long id); // soft delete
}