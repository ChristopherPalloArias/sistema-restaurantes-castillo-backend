package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "El nombre del producto es requerido")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    @NotNull(message = "El precio base es requerido")
    @Positive(message = "El precio base debe ser mayor a 0")
    private BigDecimal basePrice;
    
    @NotNull(message = "El ID de la categoría es requerido")
    private Long categoryId;
    
    private Boolean isCustomizable;
    
    private List<ProductSizeDTO> sizes;
    
    private List<ProductAccompanimentDTO> accompaniments;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductSizeDTO {
        @NotBlank(message = "El nombre del tamaño es requerido")
        private String name;
        private String description;
        @NotNull(message = "El modificador de precio es requerido")
        private BigDecimal priceModifier;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductAccompanimentDTO {
        @NotBlank(message = "El nombre del acompañamiento es requerido")
        private String name;
        private String description;
        @NotNull(message = "El modificador de precio es requerido")
        private BigDecimal priceModifier;
        private Boolean isDefault;
        private Integer maxQuantity;
    }
} 