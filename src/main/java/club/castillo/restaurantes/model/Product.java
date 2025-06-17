package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice; // Precio base del producto

    @Column(name = "is_customizable")
    private Boolean isCustomizable = false;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPrice> prices;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductSize> availableSizes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductAccompaniment> availableAccompaniments;

    @Column(nullable = false)
    private Boolean available = true; // Si está disponible en el menú

    @Column(nullable = false)
    private Boolean active = true; // Para soft delete
}
