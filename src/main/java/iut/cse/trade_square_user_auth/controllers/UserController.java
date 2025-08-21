package iut.cse.trade_square_user_auth.controllers;

import iut.cse.trade_square_user_auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/register")
    public boolean register(@RequestBody User userUs) {return  authService.createNewUser()}
}
