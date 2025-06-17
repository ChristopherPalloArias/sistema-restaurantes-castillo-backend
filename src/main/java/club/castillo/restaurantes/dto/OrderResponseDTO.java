package club.castillo.restaurantes.dto;

import club.castillo.restaurantes.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private Long userId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String tableNumber;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private String notes;
    private List<OrderItemDTO> items;

    @Data
    @Builder
    public static class OrderItemDTO {
        private Long id;
        private Long productId;
        private String productName;
        private String productDescription;
        private String productImageUrl;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal total;
        private String specialInstructions;
        private OrderItemSizeDTO selectedSize;
        private List<OrderItemAccompanimentDTO> accompaniments;
    }

    @Data
    @Builder
    public static class OrderItemSizeDTO {
        private Long id;
        private String name;
        private String description;
        private BigDecimal priceModifier;
    }

    @Data
    @Builder
    public static class OrderItemAccompanimentDTO {
        private Long id;
        private String name;
        private String description;
        private BigDecimal priceModifier;
        private Integer quantity;
    }
} 