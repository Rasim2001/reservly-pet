package com.reservly.authservice.api;

import com.reservly.authservice.dto.AuthResponse;
import com.reservly.authservice.dto.LoginRequest;
import com.reservly.authservice.dto.RegisterRequest;
import com.reservly.authservice.dto.UserResponse;
import com.reservly.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        UserResponse register = authService.register(request);

        log.info("Register account with id = {}", register.id());

        return register;
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        AuthResponse login = authService.login(request);

        log.info("Login account with token = {}", login.accessToken());

        return login;
    }
}
