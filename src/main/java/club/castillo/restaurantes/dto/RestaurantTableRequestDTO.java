package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableRequestDTO {
    @NotNull(message = "El número de mesa es requerido")
    private Integer number;

    private Long restaurantId;
    private Long zoneId;
    private String status;
}
