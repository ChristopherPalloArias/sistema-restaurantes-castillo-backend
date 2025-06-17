package club.castillo.restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuAdminResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private RestaurantDTO restaurant;
    private List<ProductDTO> products;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RestaurantDTO {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDTO {
        private Long id;
        private String name;
    }
}
