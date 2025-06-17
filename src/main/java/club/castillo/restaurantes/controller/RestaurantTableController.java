package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.RestaurantTableRequestDTO;
import club.castillo.restaurantes.dto.RestaurantTableResponseDTO;
import club.castillo.restaurantes.service.RestaurantTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableResponseDTO> createTable(@Valid @RequestBody RestaurantTableRequestDTO request) {
        return ResponseEntity.ok(tableService.createTable(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableResponseDTO> updateTable(@PathVariable Long id, @Valid @RequestBody RestaurantTableRequestDTO request) {
        return ResponseEntity.ok(tableService.updateTable(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableResponseDTO> getTable(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTableResponseDTO>> getActiveTables() {
        return ResponseEntity.ok(tableService.getAllActiveTables());
    }

    @GetMapping("/all")
    public ResponseEntity<List<RestaurantTableResponseDTO>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableTable(@PathVariable Long id) {
        tableService.disableTable(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableTable(@PathVariable Long id) {
        tableService.enableTable(id);
        return ResponseEntity.noContent().build();
    }
}
