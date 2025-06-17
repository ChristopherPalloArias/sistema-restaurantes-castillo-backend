package club.castillo.restaurantes.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String roleName;
    private Boolean active;
    private LocalDateTime createdAt;
}
