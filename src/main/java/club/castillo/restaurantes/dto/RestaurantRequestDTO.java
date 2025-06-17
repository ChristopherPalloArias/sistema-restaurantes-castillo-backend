package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequestDTO {
    @NotBlank(message = "El nombre del restaurante es requerido")
    private String name;

    private String address;

    private Long managerId;

    private String status; // abierto/cerrado
}
