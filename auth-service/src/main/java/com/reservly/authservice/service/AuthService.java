package com.reservly.authservice.service;

import com.reservly.authservice.security.AuthUser;
import com.reservly.authservice.domain.UserEntity;
import com.reservly.authservice.dto.AuthResponse;
import com.reservly.authservice.dto.LoginRequest;
import com.reservly.authservice.dto.RegisterRequest;
import com.reservly.authservice.dto.UserResponse;
import com.reservly.authservice.repository.UserRepository;
import com.reservly.common.ConflictException;
import com.reservly.common.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse register(RegisterRequest request) {

        String lowerCaseEmail = request.email().toLowerCase();

        if (userRepository.existsByEmail(lowerCaseEmail))
            throw new ConflictException("User with this email already exists");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(lowerCaseEmail);
        userEntity.setPasswordHash(passwordEncoder.encode(request.password()));
        userEntity.setCreatedAt(Instant.now());

        UserEntity saved = userRepository.save(userEntity);

        return new UserResponse(saved.getId(), saved.getEmail());
    }


    public AuthResponse login(LoginRequest request) {

        Authentication authenticate;

        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!(authenticate.getPrincipal() instanceof AuthUser user)) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return new AuthResponse(jwtService.generateToken(user.getId()));
    }
}
