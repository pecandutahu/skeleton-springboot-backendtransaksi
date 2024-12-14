package sales.sales.services;

import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import sales.sales.dto.RegisterUserRequest;
import sales.sales.dto.UserResponse;
import sales.sales.models.User;
import sales.sales.repositories.UserRepository;
import sales.sales.security.BCrypt;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void register( RegisterUserRequest request) {
        Optional<User> oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }else{
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            user.setName(request.getName());
            user.setRole(request.getRole());
            userRepository.save(user);
        }

    }

    public User get(User user) {
        return User.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse convertToUserResponse( User user ) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setName(user.getName());
        userResponse.setName(user.getName());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

}
