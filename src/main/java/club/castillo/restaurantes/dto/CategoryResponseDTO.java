package club.castillo.restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String categoryType;
    private RestaurantDTO restaurant; // null si es CLUB_STANDARD
    private Boolean active;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RestaurantDTO {
        private Long id;
        private String name;
    }
} 