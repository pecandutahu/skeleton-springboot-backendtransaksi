package sales.sales.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "sale")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

@FilterDef(name = "deletedSaleFilter", defaultCondition = "is_deleted = false")
@Filter(name = "deletedSaleFilter")
public class Sale implements Serializable{

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "sale_id", unique = true, nullable = false)
    private UUID saleId;

    @Column(name = "sale_code", unique = true, nullable = false)
    private String saleCode;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Builder.Default
    private String status = "paid"; //paid dan refund defaultnya paid, akan berubah jadi refund kalau ada trigger refund
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long updatedBy;
    
    @Builder.Default
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "saleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleItem> saleItems;

    @PrePersist
    protected void onCreate() {
        this.saleDate = LocalDateTime.now();
    }

    public List<SaleItem> getSaleItems() {
        
        return this.saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

}
