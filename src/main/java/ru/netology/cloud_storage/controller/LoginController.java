package ru.netology.cloud_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloud_storage.dto.ErrorInputData;
import ru.netology.cloud_storage.dto.LoginRequest;
import ru.netology.cloud_storage.service.auth.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
//@RequestMapping("")
public class LoginController {
    @Autowired
    AuthenticationService authenticationService;

    // Верно ли реализован logout? Как-то низкоуровнево получается..
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().body(null);
    }

    // аутентификация пользователя вручную
    // можно было еще реализуя свой userDetailService - верно?
    @PostMapping("/login")
    public ResponseEntity<ErrorInputData> login(@RequestBody LoginRequest loginRequest) {
        if (authenticationService.auth(loginRequest.getLogin(), loginRequest.getPassword())) {
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorInputData("error input data", 400));
        }
    }
}
