package iut.cse.trade_square_user_auth.services;

import iut.cse.trade_square_user_auth.models.entities.User;
import iut.cse.trade_square_user_auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        return true;
    }

    public User createNewUser(User user) {
        return userRepository.save(user);
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
