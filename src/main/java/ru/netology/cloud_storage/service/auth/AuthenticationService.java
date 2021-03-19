package ru.netology.cloud_storage.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {
    public boolean auth(String username, String password) {
        AuthenticationManager am = new AppAuthenticationManager();

        Authentication request = new UsernamePasswordAuthenticationToken(username,password);
        Authentication result = am.authenticate(request);

        SecurityContextHolder.getContext().setAuthentication(result);

        return result.isAuthenticated();
    }
}
