package sales.sales.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction implements Serializable{

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "transaction_id", unique = true, nullable = false)
    private UUID transactionId;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "total_price")
    private Double totalPrice;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long updatedBy;
    
    @Builder.Default
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "transactionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionItem> transactionItems;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }

    public List<TransactionItem> getTransactionItems() {
        
        return this.transactionItems;
    }

    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

}
