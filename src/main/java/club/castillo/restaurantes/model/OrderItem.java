package club.castillo.restaurantes.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private ProductSize selectedSize;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal total;

    private String specialInstructions;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemAccompaniment> accompaniments = new ArrayList<>();

    public void calculateTotal() {
        BigDecimal basePrice = this.unitPrice;
        
        // Aplicar modificador de precio por tamaño si existe
        if (selectedSize != null) {
            basePrice = basePrice.add(selectedSize.getPriceModifier());
        }

        // Sumar modificadores de precio de acompañamientos
        BigDecimal accompanimentTotal = accompaniments.stream()
                .map(acc -> acc.getAccompaniment().getPriceModifier()
                        .multiply(BigDecimal.valueOf(acc.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = basePrice.add(accompanimentTotal)
                .multiply(BigDecimal.valueOf(quantity));
    }

    public void addAccompaniment(OrderItemAccompaniment accompaniment) {
        accompaniments.add(accompaniment);
        accompaniment.setOrderItem(this);
    }

    public void removeAccompaniment(OrderItemAccompaniment accompaniment) {
        accompaniments.remove(accompaniment);
        accompaniment.setOrderItem(null);
    }
} 