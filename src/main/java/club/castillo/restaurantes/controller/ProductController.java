package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.ProductRequestDTO;
import club.castillo.restaurantes.dto.ProductResponseDTO;
import club.castillo.restaurantes.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllActiveProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getActiveProductsByCategory(categoryId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(productService.getActiveProductsByRestaurant(restaurantId));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableProduct(@PathVariable Long id) {
        productService.enableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        productService.setProductAvailability(id, available);
        return ResponseEntity.noContent().build();
    }

    // Gestión de precios por restaurante
    @PostMapping("/{id}/prices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setProductPrice(
            @PathVariable Long id,
            @RequestParam Long restaurantId,
            @RequestParam BigDecimal price) {
        productService.setProductPrice(id, restaurantId, price);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/prices/scheduled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setScheduledProductPrice(
            @PathVariable Long id,
            @RequestParam Long restaurantId,
            @RequestParam BigDecimal price,
            @RequestParam LocalDateTime validFrom,
            @RequestParam(required = false) LocalDateTime validUntil) {
        productService.setProductPrice(id, restaurantId, price, validFrom, validUntil);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/prices/current")
    public ResponseEntity<BigDecimal> getCurrentPrice(
            @PathVariable Long id,
            @RequestParam Long restaurantId) {
        return ResponseEntity.ok(productService.getCurrentPrice(id, restaurantId));
    }

    // Gestión de tamaños
    @PostMapping("/{id}/sizes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addSize(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO.ProductSizeDTO request) {
        productService.addProductSize(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/sizes/{sizeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateSize(
            @PathVariable Long id,
            @PathVariable Long sizeId,
            @Valid @RequestBody ProductRequestDTO.ProductSizeDTO request) {
        productService.updateProductSize(id, sizeId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/sizes/{sizeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeSize(
            @PathVariable Long id,
            @PathVariable Long sizeId) {
        productService.removeProductSize(id, sizeId);
        return ResponseEntity.noContent().build();
    }

    // Gestión de acompañamientos
    @PostMapping("/{id}/accompaniments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addAccompaniment(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO.ProductAccompanimentDTO request) {
        productService.addProductAccompaniment(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/accompaniments/{accompanimentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateAccompaniment(
            @PathVariable Long id,
            @PathVariable Long accompanimentId,
            @Valid @RequestBody ProductRequestDTO.ProductAccompanimentDTO request) {
        productService.updateProductAccompaniment(id, accompanimentId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/accompaniments/{accompanimentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAccompaniment(
            @PathVariable Long id,
            @PathVariable Long accompanimentId) {
        productService.removeProductAccompaniment(id, accompanimentId);
        return ResponseEntity.noContent().build();
    }
}
