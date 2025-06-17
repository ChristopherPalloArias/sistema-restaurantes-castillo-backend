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

    @Column(length = 500)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "category_type", nullable = false, length = 20)
    private String categoryType; // "CLUB_STANDARD" o "RESTAURANT_SPECIFIC"

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; // null si es CLUB_STANDARD

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @Column(nullable = false)
    private Boolean active = true;
}
