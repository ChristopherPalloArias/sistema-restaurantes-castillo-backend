package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "customer_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // values: 'member', 'guest'

    @OneToMany(mappedBy = "customerType")
    private List<Customer> customers;

    @Column(nullable = false)
    private Boolean active = true;
}