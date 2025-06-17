package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_sizes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSize {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 50)
    private String name; // Ejemplo: "Pequeño", "Mediano", "Grande"

    @Column(length = 200)
    private String description;

    @Column(name = "price_modifier", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceModifier; // Cantidad a sumar al precio base

    @Column(nullable = false)
    private Boolean active = true;
} 