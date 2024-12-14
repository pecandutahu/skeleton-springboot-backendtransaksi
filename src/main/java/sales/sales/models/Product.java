package sales.sales.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity


@FilterDef(name = "deletedProductFilter", defaultCondition = "is_deleted = false")
@Filter(name = "deletedProductFilter")

public class Product {
    
    @Id
    @Column(name = "product_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotEmpty(message = "Item name cannot be null.")
    @Size(min = 2, max = 200, message = ("Product name is must between 2 and 100 character"))
    @Column(name = "product_name")
    private String productName;

    @NotNull(message = "numericField: positive number value is required")
    @Min(value=0, message="numericField: positive number, min 0 is required")
    @Column(name = "stock")
    private int stock;

    @NotNull(message = "numericField: positive number value is required")
    @Min(value=0, message="numericField: positive number, min 0 is required")
    @Column(name = "price")
    private Double price;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private boolean isDeleted = false;
}
