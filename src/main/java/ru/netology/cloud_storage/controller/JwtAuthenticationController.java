package ru.netology.cloud_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloud_storage.configuration.security.JwtTokenUtil;
import ru.netology.cloud_storage.entity.dto.JwtRequest;
import ru.netology.cloud_storage.entity.dto.JwtResponse;
import ru.netology.cloud_storage.service.auth.JwtUserDetailsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class JwtAuthenticationController {
    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        authenticate(jwtRequest.getLogin(), jwtRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getLogin());

        final String jwtToken = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    private void authenticate(String login, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (DisabledException e) {
            throw new Exception("USER DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID CREDENTIALS", e);
        }
    }
}
