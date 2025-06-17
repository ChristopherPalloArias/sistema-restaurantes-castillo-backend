package club.castillo.restaurantes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String status;
    private Boolean active;
    private ManagerDTO manager;

    @Data
    @Builder
    public static class ManagerDTO {
        private Long id;
        private String name;
        private String email;
    }
}
