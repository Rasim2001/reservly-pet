package com.reservly.authservice.repository;

import com.reservly.authservice.domain.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUser_Id(Long id);
}
