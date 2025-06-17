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
public class CustomerTypeRequestDTO {
    @NotBlank(message = "El nombre del tipo de cliente es requerido")
    private String name;
}
