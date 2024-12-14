// @Entity
// @Table(name = "users")
// public class User {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String username;
//     private String password;
//     private String role; // ADMIN, CASHIER
//     private Boolean isActive;

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt = LocalDateTime.now();
//     @Column(name = "created_by", updatable = false)
//     private String createdBy;
//     private LocalDateTime updatedAt;
//     private String updatedBy;
// }
