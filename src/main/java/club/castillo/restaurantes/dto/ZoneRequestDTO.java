package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZoneRequestDTO {
    @NotBlank(message = "El nombre de la zona es requerido")
    private String name;
}
