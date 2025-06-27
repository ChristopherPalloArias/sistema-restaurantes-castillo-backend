package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.RestaurantWithAdminRequestDTO;
import club.castillo.restaurantes.dto.RestaurantRequestDTO;
import club.castillo.restaurantes.dto.RestaurantResponseDTO;
import club.castillo.restaurantes.model.Role;
import club.castillo.restaurantes.model.User;
import club.castillo.restaurantes.model.RestaurantStatus;
import club.castillo.restaurantes.repository.ZoneRepository;
import club.castillo.restaurantes.service.RestaurantService;
import club.castillo.restaurantes.service.RoleService;
import club.castillo.restaurantes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final UserService userService;
    private final RoleService roleService;
    private final RestaurantService restaurantService;
    private final ZoneRepository zoneRepository;

    @PostMapping("/restaurants-with-admin")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<RestaurantResponseDTO> createRestaurantWithAdmin(
            @Valid @RequestBody RestaurantWithAdminRequestDTO request) {

        // 1. Crear usuario administrador
        Role role = roleService.findByName("RESTAURANT_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Rol RESTAURANT_ADMIN no encontrado"));

        User newUser = User.builder()
                .name(request.getAdminName())
                .email(request.getAdminEmail())
                .password(request.getAdminPassword())
                .role(role)
                .active(true)
                .build();

        User savedAdmin = userService.save(newUser);

        // 2. Crear restaurante vinculado al usuario
        RestaurantRequestDTO restaurantDTO = new RestaurantRequestDTO();
        restaurantDTO.setName(request.getRestaurantName());
        restaurantDTO.setAddress(request.getRestaurantAddress());
        restaurantDTO.setManagerId(savedAdmin.getId());
        restaurantDTO.setStatus(RestaurantStatus.CLOSED);
        restaurantDTO.setZoneId(request.getZoneId()); // ✅ Aquí estaba el problema

        RestaurantResponseDTO savedRestaurant = restaurantService.createRestaurant(restaurantDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
    }
}
