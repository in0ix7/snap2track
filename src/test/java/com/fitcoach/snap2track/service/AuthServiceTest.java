package com.fitcoach.snap2track.service;

import com.fitcoach.snap2track.dto.AuthDto;
import com.fitcoach.snap2track.entity.User;
import com.fitcoach.snap2track.exception.DuplicateEmailException;
import com.fitcoach.snap2track.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private AuthDto.RegisterRequest validRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        validRequest = AuthDto.RegisterRequest.builder()
                .email("test@example.com")
                .password("123456")
                .name("Test User")
                .role(User.Role.CLIENT)
                .build();

        savedUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .passwordHash(new BCryptPasswordEncoder().encode("123456"))
                .name("Test User")
                .role(User.Role.CLIENT)
                .build();
    }

    @Test
    void register_Success_ShouldReturnAuthResponse() {
        // Given
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        AuthDto.AuthResponse response = authService.register(validRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getMessage()).isEqualTo("User registered successfully");

        verify(userRepository).existsByEmail(validRequest.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ShouldThrowDuplicateEmailException() {
        // Given
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.register(validRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("already exists");

        verify(userRepository).existsByEmail(validRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_ShouldEncodePassword() {
        // Given
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        authService.register(validRequest);

        // Then
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getPasswordHash()).isNotEqualTo(validRequest.getPassword());
        assertThat(capturedUser.getPasswordHash()).startsWith("$2a$"); // BCrypt pattern
    }
}