package sales.sales.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import sales.sales.dto.LoginUserRequest;
import sales.sales.dto.TokenResponse;
import sales.sales.models.User;
import sales.sales.repositories.UserRepository;
import sales.sales.security.BCrypt;
import sales.sales.security.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    private final JwtUtil jwtUtil;

    // Inject JwtUtil melalui constructor
    @Autowired
    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is not active yet, please contact admin!");
        }

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            // Generate JWT token menggunakan instance jwtUtil
            String token = jwtUtil.generateToken(user.getUsername());

            return TokenResponse.builder()
                    .token(token)
                    .expiredAt(jwtUtil.getExpiration(token).getTime()) // Ambil expiration menggunakan jwtUtil
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong");
        }
    }
}
