package club.castillo.restaurantes.model;

import club.castillo.restaurantes.model.Product;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "control_type", nullable = false, length = 20)
    private String controlType;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
