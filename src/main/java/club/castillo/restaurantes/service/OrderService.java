package club.castillo.restaurantes.service;

import club.castillo.restaurantes.dto.OrderRequestDTO;
import club.castillo.restaurantes.dto.OrderResponseDTO;
import club.castillo.restaurantes.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    /**
     * Crea un nuevo pedido.
     */
    OrderResponseDTO createOrder(OrderRequestDTO orderRequest);

    /**
     * Actualiza un pedido existente.
     */
    OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO orderRequest);

    /**
     * Obtiene un pedido por su ID.
     */
    OrderResponseDTO getOrderById(Long orderId);

    /**
     * Obtiene todos los pedidos de un restaurante con filtros opcionales.
     */
    List<OrderResponseDTO> getRestaurantOrders(Long restaurantId,
                                             Optional<OrderStatus> status,
                                             Optional<LocalDateTime> startDate,
                                             Optional<LocalDateTime> endDate);

    /**
     * Obtiene todos los pedidos de un usuario.
     */
    List<OrderResponseDTO> getUserOrders(Long userId);

    /**
     * Actualiza el estado de un pedido.
     */
    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus);

    /**
     * Cancela un pedido.
     */
    void cancelOrder(Long orderId);

    /**
     * Obtiene los pedidos activos de un restaurante (no completados ni cancelados).
     */
    List<OrderResponseDTO> getActiveOrders(Long restaurantId);

    /**
     * Obtiene los pedidos que están listos para entregar.
     */
    List<OrderResponseDTO> getReadyOrders(Long restaurantId);
} 