package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDTO {
    @NotBlank(message = "El nombre del menú es requerido")
    private String name;

    private String description;

    @NotNull(message = "El ID del restaurante es requerido")
    private Long restaurantId;
}
