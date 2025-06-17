package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_accompaniments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemAccompaniment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompaniment_id", nullable = false)
    private ProductAccompaniment accompaniment;

    @Column(nullable = false)
    private Integer quantity;
} 