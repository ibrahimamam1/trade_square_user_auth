package iut.cse.trade_square_user_auth.controllers;

import iut.cse.trade_square_user_auth.models.entities.User;
import iut.cse.trade_square_user_auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {return  userService.createNewUser(user);}

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
