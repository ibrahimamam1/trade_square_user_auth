package iut.cse.trade_square_user_auth.controllers;

import iut.cse.trade_square_user_auth.models.entities.User;
import iut.cse.trade_square_user_auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        System.out.println("register request");
        System.out.println(user);
        return  userService.createNewUser(user);
    }

    @GetMapping("/login/token")
    public Map<String, String> loginWithRefreshToken(@RequestHeader("x-refresh-token") String token) {
        System.out.println("login with token request");

        if (token == null) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        return userService.loginWithToken(token);
    }


    @GetMapping("/login/email")
    public Map<String, String> loginWithEmail(@RequestHeader("email") String email, @RequestHeader("password") String password) {
        System.out.println("login with email request");

        if (email == null || password == null) {
            throw new RuntimeException("invalid request");
        }

        return userService.loginWithEmailAndPassword(email, password);
    }

    @GetMapping("/getUserById")
    public User getUserById(Long userId){ return userService.findUserById(userId); }

    @GetMapping("/getUsersByNames")
    public List<User> getUserByName(String username){ return userService.findUsersByNames(username); }

    @GetMapping("getUserByMinRating")
    public List<User> getUserByMinRating(float minRating){ return userService.findUsersByMinRating(minRating); }

    @GetMapping("getUserByAddress")
    public List<User> getUserByAddress(String address){ return userService.findUsersByAddress(address); }

    @PutMapping("updateUser")
    public User updateUser(@RequestBody User user){return userService.updateUser(user);}
}
