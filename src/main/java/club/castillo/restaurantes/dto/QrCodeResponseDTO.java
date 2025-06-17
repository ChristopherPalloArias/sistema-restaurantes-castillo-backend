package club.castillo.restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeResponseDTO {
    private Long id;
    private Long orderId;
    private Long menuId;
    private String content;
    private LocalDateTime generatedAt;
    private Boolean active;
}
