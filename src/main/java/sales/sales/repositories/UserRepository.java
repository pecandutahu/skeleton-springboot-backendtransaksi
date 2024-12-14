package sales.sales.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sales.sales.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(String token);
    Optional<User> findByRole(String role);
}
