package com.fitcoach.snap2track.service;

import com.fitcoach.snap2track.dto.AuthDto;
import com.fitcoach.snap2track.entity.User;
import com.fitcoach.snap2track.exception.DuplicateEmailException;
import com.fitcoach.snap2track.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        // Проверка: email уже существует?
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Хэшируем пароль с помощью BCrypt
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordHash)
                .name(request.getName())
                .role(request.getRole())
                .build();

        User saved = userRepository.save(user);
        log.info("Registered new user: {} with role {}", saved.getEmail(), saved.getRole());

        return AuthDto.AuthResponse.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .name(saved.getName())
                .role(saved.getRole())
                .message("User registered successfully")
                .build();
    }
}