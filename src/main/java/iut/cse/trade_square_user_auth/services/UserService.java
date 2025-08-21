package iut.cse.trade_square_user_auth.services;

import iut.cse.trade_square_user_auth.models.entities.User;
import iut.cse.trade_square_user_auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    public boolean authenticate(String username, String password) {
        return true;
    }
    public User createNewUser(User user) {
        return userRepository.save(user);
    }
}
