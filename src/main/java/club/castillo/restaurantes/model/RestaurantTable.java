package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Column(length = 20)
    private String status = "available";

    @Column(nullable = false)
    private Boolean active = true;

}