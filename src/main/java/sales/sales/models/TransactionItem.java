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

@Table(name = "transaction_item")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionItem {
    @Id
    @Column(name = "transaction_item_id")
    @GeneratedValue(generator = "UUID")
    private UUID orderItemId;
    
    @Column(name = "transaction_id")
    private UUID transactionId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "subtotal")
    private int subtotal;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private int createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private int updatedBy;
    
    private boolean isDeleted = false;

    public TransactionItem(Product product, int quantity, UUID transactionId) {
        this.product = product;
        this.quantity = quantity;
        this.transactionId = transactionId;
    }
}
