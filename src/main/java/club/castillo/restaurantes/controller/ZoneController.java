package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.ZoneRequestDTO;
import club.castillo.restaurantes.dto.ZoneResponseDTO;
import club.castillo.restaurantes.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneResponseDTO> createZone(@Valid @RequestBody ZoneRequestDTO request) {
        return ResponseEntity.ok(zoneService.createZone(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneResponseDTO> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneRequestDTO request) {
        return ResponseEntity.ok(zoneService.updateZone(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponseDTO> getZone(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @GetMapping
    public ResponseEntity<List<ZoneResponseDTO>> getActiveZones() {
        return ResponseEntity.ok(zoneService.getAllActiveZones());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ZoneResponseDTO>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableZone(@PathVariable Long id) {
        zoneService.disableZone(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableZone(@PathVariable Long id) {
        zoneService.enableZone(id);
        return ResponseEntity.noContent().build();
    }
}
