package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.CustomerTypeRequestDTO;
import club.castillo.restaurantes.dto.CustomerTypeResponseDTO;
import club.castillo.restaurantes.service.CustomerTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-types")
@RequiredArgsConstructor
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerTypeResponseDTO> createCustomerType(@Valid @RequestBody CustomerTypeRequestDTO request) {
        return ResponseEntity.ok(customerTypeService.createCustomerType(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerTypeResponseDTO> updateCustomerType(
            @PathVariable Long id,
            @Valid @RequestBody CustomerTypeRequestDTO request) {
        return ResponseEntity.ok(customerTypeService.updateCustomerType(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeResponseDTO> getCustomerType(@PathVariable Long id) {
        return ResponseEntity.ok(customerTypeService.getCustomerTypeById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerTypeResponseDTO>> getActiveCustomerTypes() {
        return ResponseEntity.ok(customerTypeService.getAllActiveCustomerTypes());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerTypeResponseDTO>> getAllCustomerTypes() {
        return ResponseEntity.ok(customerTypeService.getAllCustomerTypes());
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableCustomerType(@PathVariable Long id) {
        customerTypeService.disableById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableCustomerType(@PathVariable Long id) {
        customerTypeService.enableById(id);
        return ResponseEntity.noContent().build();
    }
}
