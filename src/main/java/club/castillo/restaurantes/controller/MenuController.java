package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.MenuResponseDTO;
import club.castillo.restaurantes.dto.MenuRequestDTO;
import club.castillo.restaurantes.dto.MenuAdminResponseDTO;
import club.castillo.restaurantes.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<MenuResponseDTO> getRestaurantMenu(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "false") boolean showUnavailable,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String searchTerm) {
        
        return ResponseEntity.ok(menuService.getRestaurantMenu(
                restaurantId,
                showUnavailable,
                Optional.ofNullable(maxPrice),
                Optional.ofNullable(searchTerm)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuAdminResponseDTO> createMenu(@RequestBody @Valid MenuRequestDTO request) {
        return ResponseEntity.ok(menuService.createMenu(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuAdminResponseDTO> updateMenu(
            @PathVariable Long id,
            @RequestBody @Valid MenuRequestDTO request) {
        return ResponseEntity.ok(menuService.updateMenu(id, request));
    }

    @PostMapping("/{id}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignProduct(
            @PathVariable Long id,
            @PathVariable Long productId) {
        menuService.assignProduct(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unassignProduct(
            @PathVariable Long id,
            @PathVariable Long productId) {
        menuService.unassignProduct(id, productId);
        return ResponseEntity.noContent().build();
    }
}
