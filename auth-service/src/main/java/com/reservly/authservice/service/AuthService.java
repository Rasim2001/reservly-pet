package com.reservly.authservice.service;

import com.reservly.authservice.domain.RefreshTokenEntity;
import com.reservly.authservice.domain.UserRole;
import com.reservly.authservice.dto.*;
import com.reservly.authservice.security.AuthUser;
import com.reservly.authservice.domain.UserEntity;
import com.reservly.authservice.repository.UserRepository;
import com.reservly.common.ConflictException;
import com.reservly.common.TooManyRequestsException;
import com.reservly.common.UnauthorizedException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final TokenDenylistService tokenDenylistService;
    private final LoginAttemptService  loginAttemptService;

    @Transactional
    public UserResponse register(RegisterRequest request) {

        String lowerCaseEmail = request.email().toLowerCase();

        if (userRepository.existsByEmail(lowerCaseEmail))
            throw new ConflictException("User with this email already exists");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(lowerCaseEmail);
        userEntity.setPasswordHash(passwordEncoder.encode(request.password()));
        userEntity.setCreatedAt(Instant.now());
        userEntity.setRole(UserRole.USER);

        UserEntity saved = userRepository.save(userEntity);

        return new UserResponse(saved.getId(), saved.getEmail());
    }

    @Transactional()
    public AuthResponse login(LoginRequest request) {

        Authentication authenticate;
        String email = request.email().toLowerCase();

        if (loginAttemptService.isBlocked(email)) {
            throw new TooManyRequestsException("Too many requests");
        }

        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (AuthenticationException e) {
            loginAttemptService.recordFailure(email);
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!(authenticate.getPrincipal() instanceof AuthUser user)) {
            throw new UnauthorizedException("Invalid email or password");
        }

        loginAttemptService.reset(email);

        String accessToken =jwtService.generateToken(user.getId(), user.getRole());
        UserEntity userEntity = userRepository.getReferenceById(user.getId());
        String refreshToken = refreshTokenService.create(userEntity);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        RefreshTokenEntity oldRefreshEntity = refreshTokenService.validate(request.refreshToken());
        UserEntity userEntity = oldRefreshEntity.getUser();

        refreshTokenService.deleteByToken(request.refreshToken());

        String accessToken = jwtService.generateToken(userEntity.getId(), userEntity.getRole());
        String refreshToken = refreshTokenService.create(userEntity);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public void logout(LogoutRequest request, String accessToken) {

        Claims claims = jwtService.parseToken(accessToken);
        Duration ttl = Duration.between(Instant.now(), claims.getExpiration().toInstant());

        if(!ttl.isNegative()){
            tokenDenylistService.denylist(claims.getId(), ttl);
        }

        refreshTokenService.deleteByToken(request.refreshToken());
    }
}
