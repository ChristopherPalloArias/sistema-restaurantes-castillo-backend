package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Boolean active = true;

}
