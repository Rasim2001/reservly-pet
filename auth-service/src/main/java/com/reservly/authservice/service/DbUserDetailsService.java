package com.reservly.authservice.service;

import com.reservly.authservice.security.AuthUser;
import com.reservly.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return userRepository.findByEmail(username.toLowerCase())
                    .map(u -> new AuthUser(u.getId(), u.getEmail().toLowerCase(), u.getPasswordHash(), List.of()))
                    .orElseThrow(() -> new UsernameNotFoundException("User with email = " + username + "not found"));
    }
}
