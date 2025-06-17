package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
    @NotBlank(message = "El nombre del cliente es requerido")
    private String name;

    private String identification;

    @Email(message = "El email debe ser válido")
    private String email;

    @NotNull(message = "El tipo de cliente es requerido")
    private Long customerTypeId;

    private String status;

    private LocalDate joinedAt;
}
