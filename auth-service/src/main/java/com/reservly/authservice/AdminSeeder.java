package com.reservly.authservice;

import com.reservly.authservice.domain.UserEntity;
import com.reservly.authservice.domain.UserRole;
import com.reservly.authservice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;


    @Override
    public void run(ApplicationArguments args) {
        String email = adminEmail.toLowerCase();

        if (userRepository.existsByEmail(email)) {
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setEmail(email);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(UserRole.ADMIN);
        admin.setCreatedAt(Instant.now());

        userRepository.save(admin);
    }
}
