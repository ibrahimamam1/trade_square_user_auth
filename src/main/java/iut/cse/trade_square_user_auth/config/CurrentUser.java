package iut.cse.trade_square_user_auth.config;

import iut.cse.trade_square_user_auth.models.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public class CurrentUser {
    public static UserDetails get(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) auth;
        return (UserDetails) authToken.getPrincipal();
    }

    public static User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) auth;
        return (User) authToken.getPrincipal();
    }
}
