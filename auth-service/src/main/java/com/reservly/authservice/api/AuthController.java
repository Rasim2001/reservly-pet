package com.reservly.authservice.api;

import com.reservly.authservice.dto.*;
import com.reservly.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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

        log.info("Login successful");

        return login;
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody @Valid RefreshRequest request) {

        log.info("refresh token");

        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid LogoutRequest request) {
        log.info("logout happened");

        authService.logout(request);
    }
}
