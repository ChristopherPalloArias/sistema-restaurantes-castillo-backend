package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.model.Role;
import club.castillo.restaurantes.repository.RoleRepository;
import club.castillo.restaurantes.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}
