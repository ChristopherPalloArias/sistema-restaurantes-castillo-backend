package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.RestaurantRequestDTO;
import club.castillo.restaurantes.dto.RestaurantResponseDTO;
import club.castillo.restaurantes.service.RestaurantService;
import club.castillo.restaurantes.model.RestaurantStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantRequestDTO request) {
        return ResponseEntity.ok(restaurantService.createRestaurant(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequestDTO request) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getActiveRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllActiveRestaurants());
    }

    @GetMapping("/all")
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByStatus(
            @PathVariable RestaurantStatus status) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurantStatus(
            @PathVariable Long id,
            @RequestParam RestaurantStatus status) {
        return ResponseEntity.ok(restaurantService.updateRestaurantStatus(id, status));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableRestaurant(@PathVariable Long id) {
        restaurantService.disableRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> enableRestaurant(@PathVariable Long id) {
        restaurantService.enableRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
