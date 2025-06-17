package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.CustomerRequestDTO;
import club.castillo.restaurantes.dto.CustomerResponseDTO;
import club.castillo.restaurantes.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getActiveCustomers() {
        return ResponseEntity.ok(customerService.getAllActiveCustomers());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableCustomer(@PathVariable Long id) {
        customerService.disableById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableCustomer(@PathVariable Long id) {
        customerService.enableById(id);
        return ResponseEntity.noContent().build();
    }
}
