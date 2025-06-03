package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String identification;

    @Column(length = 100)
    private String email;

    @ManyToOne @JoinColumn(name = "customer_type_id")
    private CustomerType customerType;

    @Column(length = 20)
    private String status = "active";

    @Column(name = "joined_at")
    private LocalDate joinedAt;

    @Column(nullable = false)
    private Boolean active = true;
}
