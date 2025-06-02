package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt = LocalDateTime.now();
}
