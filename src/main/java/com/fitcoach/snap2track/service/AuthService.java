package com.fitcoach.snap2track.service;

import com.fitcoach.snap2track.dto.AuthDto;
import com.fitcoach.snap2track.entity.User;
import com.fitcoach.snap2track.exception.DuplicateEmailException;
import com.fitcoach.snap2track.exception.InvalidCredentialsException;
import com.fitcoach.snap2track.repository.UserRepository;
import com.fitcoach.snap2track.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

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

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        return AuthDto.LoginResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}