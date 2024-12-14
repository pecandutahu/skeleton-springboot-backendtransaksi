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
import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Transactional
     public TokenResponse login(LoginUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong"));
        if(!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is not active yet, please contact admin!");
        }
        if(BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);
            return  TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis()  +  (1000L * 60 * 60 * 24 * 30) ;
    }


    
}
