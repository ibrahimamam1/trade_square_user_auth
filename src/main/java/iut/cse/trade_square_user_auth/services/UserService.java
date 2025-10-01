package iut.cse.trade_square_user_auth.services;

import iut.cse.trade_square_user_auth.config.JWTServiceImpl;
import iut.cse.trade_square_user_auth.models.entities.RefreshToken;
import iut.cse.trade_square_user_auth.models.entities.User;
import iut.cse.trade_square_user_auth.repositories.RefreshTokenRepository;
import iut.cse.trade_square_user_auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public Map<String, String> loginWithToken(String refreshToken) {
        // 1. Find refresh token entry
        Optional<RefreshToken> optional = refreshTokenRepository.findByToken(refreshToken);
        if (optional.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }

        RefreshToken refreshTokenEntity = optional.get();
        Long userId = refreshTokenEntity.getUserId();

        // Validate refresh token still matches DB
        if (!jwtService.validateRefreshToken(userId, refreshToken)) {
            throw new RuntimeException("Refresh token not valid for this user");
        }

        // Load user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Wrap into UserDetails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        // Generate new access token
        String newAccessToken = jwtService.generateToken(userDetails);

        // Return response
        Map<String, String> response = new HashMap<>();
        response.put("user_id", user.getId().toString());
        response.put("token", newAccessToken);
        response.put("refresh_token", refreshToken); // same refresh token returned
        return response;
    }

    public Map<String, String> loginWithEmailAndPassword(String email, String password) {
        // 1. Load user by email
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new RuntimeException("Invalid email or password");

        // 2. Validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Build UserDetails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        // 4. Generate tokens
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(Map.of(), userDetails);

        // Save refresh token in DB
        jwtService.saveRefreshToken(user.getId(), refreshToken);

        // 5. Return response
        Map<String, String> response = new HashMap<>();
        response.put("user_id", user.getId().toString());
        response.put("token", accessToken);
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
