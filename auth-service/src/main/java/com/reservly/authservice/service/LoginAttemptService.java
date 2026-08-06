package com.reservly.authservice.service;

import com.reservly.common.RedisKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class LoginAttemptService {

    private final StringRedisTemplate redisTemplate;
    private final int maxAttempts;
    private final Duration window;

    public LoginAttemptService(
            StringRedisTemplate redisTemplate,
            @Value("${app.security.login.max-attempts}") int maxAttempts,
            @Value("${app.security.login.window}") Duration window) {

        this.redisTemplate = redisTemplate;
        this.maxAttempts = maxAttempts;
        this.window = window;
    }

    public boolean isBlocked(String email) {
        String attempts = redisTemplate.opsForValue().get(RedisKeys.LOGIN_FAIL_PREFIX + email);

        return attempts != null && Integer.parseInt(attempts) >= maxAttempts;
    }

    public void recordFailure(String email) {
        String key = RedisKeys.LOGIN_FAIL_PREFIX + email;

        Long attempts = redisTemplate.opsForValue().increment(key);

        if(attempts != null && attempts == 1){
            redisTemplate.expire(key, window);
        }

        log.info("record failure with email : {}", email);
    }

    public void reset(String email) {
        redisTemplate.delete(RedisKeys.LOGIN_FAIL_PREFIX + email);
    }
}
