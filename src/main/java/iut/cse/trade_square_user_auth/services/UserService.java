package iut.cse.trade_square_user_auth.services;

import iut.cse.trade_square_user_auth.config.JWTServiceImpl;
import iut.cse.trade_square_user_auth.models.entities.User;
import iut.cse.trade_square_user_auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtService;

    public boolean authenticate(String username, String password) {
        return true;
    }

    public Map<String, String> createNewUser(User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);

        // Wrap into UserDetails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities("USER") // or roles you want
                .build();

        // Generate JWT
        String token = jwtService.generateToken(userDetails);

        // Generate refresh token
        String refreshToken = jwtService.generateRefreshToken(Map.of(), userDetails);
        jwtService.saveRefreshToken(savedUser.getId(), refreshToken);

        // Return response
        Map<String, String> response = new HashMap<>();
        response.put("user_id", savedUser.getId().toString());
        response.put("token", token);
        response.put("refresh_token", refreshToken);
        return response;
    }

    public User findUserById(Long id) {return userRepository.findById(id).get();}

    public List<User> findUsersByNames(String username) {
        String[] names = username.split(" ", 2);
        String firstname = names[0];
        String lastname = names.length > 1 ? names[1] : " ";
        return userRepository.findByUserNames(firstname, lastname);
    }

    public List<User> findUsersByMinRating(float minRating) {return userRepository.findByMinRating(minRating);}

    public List<User> findUsersByAddress(String address) {return userRepository.findByAddress(address);}

    public User updateUser(User user) {return userRepository.save(user);}
}
