package club.castillo.restaurantes.service;

import club.castillo.restaurantes.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findById(Long id);
}
