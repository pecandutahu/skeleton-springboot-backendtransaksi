package sales.sales.models;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String password;
    private String name;
    private String role; // Untuk sementara hardcode dulu admin,kasir
    private String token;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long updatedBy;
    
    @Builder.Default
    private boolean isDeleted = false;
    
    @Builder.Default
    private boolean isActive = false;
    
    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

}
