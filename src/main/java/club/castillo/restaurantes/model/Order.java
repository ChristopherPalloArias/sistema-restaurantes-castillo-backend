package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne @JoinColumn(name = "table_id")
    private RestaurantTable table;

    @Column(length = 20)
    private String status = "pending";

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> items;

    @OneToOne(mappedBy = "order")
    private QrCode qrCodeRef;

    @Column(nullable = false)
    private Boolean active = true;

}