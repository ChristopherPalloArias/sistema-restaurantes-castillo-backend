package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.math.BigDecimal;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Column(name = "fixed_price_club")
    private Boolean fixedPriceClub = false;

    private Boolean available = true;

    @ManyToOne @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "product")
    private List<ProductPrice> customPrices;

    @Column(nullable = false)
    private Boolean active = true;

}
