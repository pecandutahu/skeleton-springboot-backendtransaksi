package sales.sales.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "sale_item")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SaleItem {
    @Id
    @Column(name = "sale_item_id")
    @GeneratedValue(generator = "UUID")
    private UUID saleItemId;
    
    @Column(name = "sale_id")
    private UUID saleId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "subtotal")
    private Double subtotal;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long updatedBy;
    
    private boolean isDeleted = false;

    public SaleItem(Product product, int quantity, UUID saleId) {
        this.product = product;
        this.quantity = quantity;
        this.saleId = saleId;
    }
}
