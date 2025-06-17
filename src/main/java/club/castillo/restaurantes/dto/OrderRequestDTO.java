package club.castillo.restaurantes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    @NotNull(message = "El ID del restaurante es requerido")
    private Long restaurantId;

    private Long userId;

    @NotBlank(message = "El nombre del cliente es requerido")
    @Size(max = 100, message = "El nombre del cliente no puede exceder los 100 caracteres")
    private String customerName;

    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    private String customerPhone;

    @Email(message = "El email debe ser válido")
    private String customerEmail;

    @NotBlank(message = "El número de mesa es requerido")
    @Size(max = 10, message = "El número de mesa no puede exceder los 10 caracteres")
    private String tableNumber;

    @Size(max = 500, message = "Las notas no pueden exceder los 500 caracteres")
    private String notes;

    @NotEmpty(message = "El pedido debe tener al menos un item")
    @Valid
    private List<OrderItemRequestDTO> items;

    @Data
    public static class OrderItemRequestDTO {
        @NotNull(message = "El ID del producto es requerido")
        private Long productId;

        private Long sizeId;

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        @Max(value = 99, message = "La cantidad no puede exceder 99")
        private Integer quantity;

        @Size(max = 255, message = "Las instrucciones especiales no pueden exceder los 255 caracteres")
        private String specialInstructions;

        @Valid
        private List<OrderItemAccompanimentRequestDTO> accompaniments;
    }

    @Data
    public static class OrderItemAccompanimentRequestDTO {
        @NotNull(message = "El ID del acompañamiento es requerido")
        private Long accompanimentId;

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        @Max(value = 99, message = "La cantidad no puede exceder 99")
        private Integer quantity;
    }
} 