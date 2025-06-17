package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.MenuResponseDTO;
import club.castillo.restaurantes.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
} 