package com.reservly.authservice.service;

import com.reservly.authservice.domain.RefreshTokenEntity;
import com.reservly.authservice.domain.UserEntity;
import com.reservly.authservice.repository.RefreshTokenRepository;
import com.reservly.common.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final RefreshTokenRepository repository;

    @Value("${app.jwt.refresh-ttl}")
    private Duration refreshTtl;

    @Transactional
    public String create(UserEntity userEntity) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(generateToken());
        refreshTokenEntity.setUser(userEntity);
        refreshTokenEntity.setCreatedAt(Instant.now());
        refreshTokenEntity.setExpiresAt(Instant.now().plus(refreshTtl));

        log.info("create refresh token to userId : {}", userEntity.getId());

        RefreshTokenEntity saved = repository.save(refreshTokenEntity);

        return saved.getToken();
    }

    @Transactional
    public void deleteByToken(String token) {
        log.info("delete refresh token");

        repository.deleteByToken(token);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        log.info("delete by user_id : {}", userId);

        repository.deleteByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public RefreshTokenEntity validate(String token) {
        RefreshTokenEntity entity = repository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if(entity.getExpiresAt().isBefore(Instant.now())){
            throw new UnauthorizedException("Refresh token expired");
        }

        return entity;
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
