package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByActiveTrue();
    boolean existsByEmail(String email);
}
