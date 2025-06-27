package club.castillo.restaurantes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantWithAdminRequestDTO {

    @NotBlank
    private String restaurantName;

    private String restaurantAddress;

    private Long zoneId;

    @NotBlank
    private String adminName;

    @Email
    @NotBlank
    private String adminEmail;

    @NotBlank
    private String adminPassword;
}
