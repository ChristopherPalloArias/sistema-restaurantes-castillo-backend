package club.castillo.restaurantes.service.impl;

import club.castillo.restaurantes.dto.OrderRequestDTO;
import club.castillo.restaurantes.dto.OrderResponseDTO;
import club.castillo.restaurantes.model.*;
import club.castillo.restaurantes.repository.*;
import club.castillo.restaurantes.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductAccompanimentRepository productAccompanimentRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        // Validar restaurante
        Restaurant restaurant = restaurantRepository.findById(orderRequest.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante no encontrado"));

        // Validar usuario si se proporciona
        User user = null;
        if (orderRequest.getUserId() != null) {
            user = userRepository.findById(orderRequest.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        }

        // Crear orden
        Order order = Order.builder()
                .restaurant(restaurant)
                .user(user)
                .customerName(orderRequest.getCustomerName())
                .customerPhone(orderRequest.getCustomerPhone())
                .customerEmail(orderRequest.getCustomerEmail())
                .tableNumber(orderRequest.getTableNumber())
                .notes(orderRequest.getNotes())
                .status(OrderStatus.PENDING)
                .subtotal(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        // Procesar items
        orderRequest.getItems().forEach(itemRequest -> {
            OrderItem item = createOrderItem(order, itemRequest);
            order.addItem(item);
        });

        // Calcular totales
        order.calculateTotals();

        // Guardar orden
        Order savedOrder = orderRepository.save(order);

        return mapToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        // Solo se pueden actualizar pedidos pendientes
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden actualizar pedidos pendientes");
        }

        // Actualizar datos básicos
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerPhone(orderRequest.getCustomerPhone());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setTableNumber(orderRequest.getTableNumber());
        order.setNotes(orderRequest.getNotes());

        // Eliminar items existentes
        order.getItems().clear();

        // Procesar nuevos items
        orderRequest.getItems().forEach(itemRequest -> {
            OrderItem item = createOrderItem(order, itemRequest);
            order.addItem(item);
        });

        // Recalcular totales
        order.calculateTotals();

        // Guardar cambios
        Order updatedOrder = orderRepository.save(order);

        return mapToResponseDTO(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        return mapToResponseDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getRestaurantOrders(Long restaurantId,
                                                    Optional<OrderStatus> status,
                                                    Optional<LocalDateTime> startDate,
                                                    Optional<LocalDateTime> endDate) {
        List<Order> orders;
        if (status.isPresent() && startDate.isPresent() && endDate.isPresent()) {
            orders = orderRepository.findByRestaurantIdAndStatusAndCreatedAtBetween(
                    restaurantId, status.get(), startDate.get(), endDate.get());
        } else if (status.isPresent()) {
            orders = orderRepository.findByRestaurantIdAndStatus(restaurantId, status.get());
        } else if (startDate.isPresent() && endDate.isPresent()) {
            orders = orderRepository.findByRestaurantIdAndCreatedAtBetween(
                    restaurantId, startDate.get(), endDate.get());
        } else {
            orders = orderRepository.findByRestaurantId(restaurantId);
        }

        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        // Validar transición de estado
        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return mapToResponseDTO(updatedOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos pendientes");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getActiveOrders(Long restaurantId) {
        List<Order> orders = orderRepository.findActiveByRestaurantId(restaurantId);
        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getReadyOrders(Long restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantIdAndStatus(restaurantId, OrderStatus.READY);
        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private OrderItem createOrderItem(Order order, OrderRequestDTO.OrderItemRequestDTO itemRequest) {
        // Validar producto
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        // Validar tamaño si se proporciona
        ProductSize size = null;
        if (itemRequest.getSizeId() != null) {
            size = productSizeRepository.findById(itemRequest.getSizeId())
                    .orElseThrow(() -> new EntityNotFoundException("Tamaño no encontrado"));
            if (!size.getProduct().getId().equals(product.getId())) {
                throw new IllegalArgumentException("El tamaño no pertenece al producto");
            }
        }

        // Obtener precio actual
        BigDecimal currentPrice = productPriceRepository
                .findCurrentPrice(product.getId(), order.getRestaurant().getId(), LocalDateTime.now())
                .map(ProductPrice::getPrice)
                .orElse(product.getBasePrice());

        // Crear item
        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .selectedSize(size)
                .quantity(itemRequest.getQuantity())
                .unitPrice(currentPrice)
                .specialInstructions(itemRequest.getSpecialInstructions())
                .build();

        // Procesar acompañamientos si existen
        if (itemRequest.getAccompaniments() != null) {
            itemRequest.getAccompaniments().forEach(accRequest -> {
                ProductAccompaniment accompaniment = productAccompanimentRepository
                        .findById(accRequest.getAccompanimentId())
                        .orElseThrow(() -> new EntityNotFoundException("Acompañamiento no encontrado"));

                if (!accompaniment.getProduct().getId().equals(product.getId())) {
                    throw new IllegalArgumentException("El acompañamiento no pertenece al producto");
                }

                OrderItemAccompaniment orderItemAcc = OrderItemAccompaniment.builder()
                        .orderItem(item)
                        .accompaniment(accompaniment)
                        .quantity(accRequest.getQuantity())
                        .build();

                item.addAccompaniment(orderItemAcc);
            });
        }

        // Calcular total del item
        item.calculateTotal();

        return item;
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("No se puede cambiar el estado de un pedido cancelado o entregado");
        }

        // Validar secuencia lógica de estados
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Un pedido pendiente solo puede ser confirmado o cancelado");
                }
                break;
            case CONFIRMED:
                if (newStatus != OrderStatus.IN_PREPARATION) {
                    throw new IllegalStateException("Un pedido confirmado solo puede pasar a preparación");
                }
                break;
            case IN_PREPARATION:
                if (newStatus != OrderStatus.READY) {
                    throw new IllegalStateException("Un pedido en preparación solo puede pasar a listo");
                }
                break;
            case READY:
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new IllegalStateException("Un pedido listo solo puede pasar a entregado");
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new IllegalStateException("No se puede cambiar el estado de un pedido entregado o cancelado");
            default:
                throw new IllegalStateException("Estado de pedido no válido");
        }
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .restaurantId(order.getRestaurant().getId())
                .restaurantName(order.getRestaurant().getName())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerEmail(order.getCustomerEmail())
                .tableNumber(order.getTableNumber())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .completedAt(order.getCompletedAt())
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .total(order.getTotal())
                .notes(order.getNotes())
                .items(mapOrderItems(order.getItems()))
                .build();
    }

    private List<OrderResponseDTO.OrderItemDTO> mapOrderItems(List<OrderItem> items) {
        return items.stream()
                .map(this::mapOrderItemToDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO.OrderItemDTO mapOrderItemToDTO(OrderItem item) {
        return OrderResponseDTO.OrderItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productDescription(item.getProduct().getDescription())
                .productImageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .total(item.getTotal())
                .specialInstructions(item.getSpecialInstructions())
                .selectedSize(item.getSelectedSize() != null ? mapOrderItemSize(item.getSelectedSize()) : null)
                .accompaniments(mapOrderItemAccompaniments(item.getAccompaniments()))
                .build();
    }

    private OrderResponseDTO.OrderItemSizeDTO mapOrderItemSize(ProductSize size) {
        return OrderResponseDTO.OrderItemSizeDTO.builder()
                .id(size.getId())
                .name(size.getName())
                .description(size.getDescription())
                .priceModifier(size.getPriceModifier())
                .build();
    }

    private List<OrderResponseDTO.OrderItemAccompanimentDTO> mapOrderItemAccompaniments(
            List<OrderItemAccompaniment> accompaniments) {
        return accompaniments.stream()
                .map(acc -> OrderResponseDTO.OrderItemAccompanimentDTO.builder()
                        .id(acc.getAccompaniment().getId())
                        .name(acc.getAccompaniment().getName())
                        .description(acc.getAccompaniment().getDescription())
                        .priceModifier(acc.getAccompaniment().getPriceModifier())
                        .quantity(acc.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
} 