package club.castillo.restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal basePrice;
    private CategoryResponseDTO category;
    private Boolean isCustomizable;
    private Boolean available;
    private Boolean active;
    
    private List<ProductSizeDTO> availableSizes;
    private List<ProductAccompanimentDTO> availableAccompaniments;
    private Map<Long, BigDecimal> restaurantPrices; // Map de restaurantId a precio

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductSizeDTO {
        private Long id;
        private String name;
        private String description;
        private BigDecimal priceModifier;
        private Boolean active;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductAccompanimentDTO {
        private Long id;
        private String name;
        private String description;
        private BigDecimal priceModifier;
        private Boolean isDefault;
        private Integer maxQuantity;
        private Boolean active;
    }
} 