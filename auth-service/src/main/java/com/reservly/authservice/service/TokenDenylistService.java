package com.reservly.authservice.service;

import com.reservly.common.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenDenylistService {

    private final StringRedisTemplate redisTemplate;

    public void denylist(String jti, Duration ttl) {
        redisTemplate.opsForValue().set(RedisKeys.DENYLIST_PREFIX + jti, "1", ttl);
    }
}
