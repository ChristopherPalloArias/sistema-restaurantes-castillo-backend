package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_accompaniments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAccompaniment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(name = "price_modifier", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceModifier; // Precio adicional del acompañamiento

    @Column(name = "is_default")
    private Boolean isDefault = false; // Si viene por defecto con el producto

    @Column(name = "max_quantity")
    private Integer maxQuantity; // Cantidad máxima permitida, null si no hay límite

    @Column(nullable = false)
    private Boolean active = true;
} 