package club.castillo.restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String identification;
    private String email;
    private String status;
    private LocalDate joinedAt;
    private Boolean active;
    private CustomerTypeDTO customerType;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerTypeDTO {
        private Long id;
        private String name;
    }
}
