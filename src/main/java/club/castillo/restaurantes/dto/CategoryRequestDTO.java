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
public class CategoryRequestDTO {
    @NotBlank(message = "El nombre de la categoría es requerido")
    private String name;
    
    private String description;
    
    private String imageUrl;

    @NotNull(message = "El tipo de categoría es requerido")
    private String categoryType; // "CLUB_STANDARD" o "RESTAURANT_SPECIFIC"

    private Long restaurantId; // Requerido solo si categoryType es "RESTAURANT_SPECIFIC"
} 