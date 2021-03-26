package ru.netology.cloud_storage.configuration.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloud_storage.service.auth.JwtUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// for any incoming request - checking if the request has a valid JWT token

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "auth-token";

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

        String login = null;
        String jwtToken = null;

        // Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ") && !requestTokenHeader.equals("Bearer null")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                login = jwtTokenUtil.getUserNameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                // TODO: Throw EXCEPTION
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                // TODO: Throw EXCEPTION
                System.out.println("JWT Token has expired");
            }
        } else {
            // TODO: Throw EXCEPTION
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(login);

            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}