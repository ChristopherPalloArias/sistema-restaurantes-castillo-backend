package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User manager;

    @Column(length = 20)
    private String status = "closed";

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantTable> tables;
}
