package ru.netology.cloud_storage.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.Collection;

public class AppAuthenticationManager implements AuthenticationManager {
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        Collection<GrantedAuthority> roles = new ArrayList<>();

        if ("user1".equals(auth.getPrincipal()) & "password1".equals(auth.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(), roles);
        } else {
            auth.setAuthenticated(false);
            return auth;
        }


//        throw new BadCredentialsException("");
    }
}
