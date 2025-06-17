package club.castillo.restaurantes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MenuResponseDTO {
    private Long restaurantId;
    private String restaurantName;
    private List<MenuCategoryDTO> categories;

    @Data
    @Builder
    public static class MenuCategoryDTO {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private List<MenuProductDTO> products;
    }

    @Data
    @Builder
    public static class MenuProductDTO {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private Double currentPrice;
        private Boolean available;
        private Boolean isCustomizable;
        private List<MenuProductSizeDTO> availableSizes;
        private List<MenuProductAccompanimentDTO> availableAccompaniments;
    }

    @Data
    @Builder
    public static class MenuProductSizeDTO {
        private Long id;
        private String name;
        private String description;
        private Double priceModifier;
    }

    @Data
    @Builder
    public static class MenuProductAccompanimentDTO {
        private Long id;
        private String name;
        private String description;
        private Double priceModifier;
        private Boolean isDefault;
        private Integer maxQuantity;
    }
} 