package club.castillo.restaurantes.controller;

import club.castillo.restaurantes.dto.OrderRequestDTO;
import club.castillo.restaurantes.dto.OrderResponseDTO;
import club.castillo.restaurantes.model.OrderStatus;
import club.castillo.restaurantes.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponseDTO>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        return ResponseEntity.ok(orderService.getRestaurantOrders(
                restaurantId,
                Optional.ofNullable(status),
                Optional.ofNullable(startDate),
                Optional.ofNullable(endDate)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurant/{restaurantId}/active")
    public ResponseEntity<List<OrderResponseDTO>> getActiveOrders(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.getActiveOrders(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}/ready")
    public ResponseEntity<List<OrderResponseDTO>> getReadyOrders(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.getReadyOrders(restaurantId));
    }
} 