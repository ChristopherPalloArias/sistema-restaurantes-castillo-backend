package club.castillo.restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableResponseDTO {
    private Long id;
    private Integer number;
    private String status;
    private Boolean active;
    private RestaurantDTO restaurant;
    private ZoneDTO zone;

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
    public static class ZoneDTO {
        private Long id;
        private String name;
    }
}
