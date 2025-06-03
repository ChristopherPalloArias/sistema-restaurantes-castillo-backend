package club.castillo.restaurantes.repository;

import club.castillo.restaurantes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
