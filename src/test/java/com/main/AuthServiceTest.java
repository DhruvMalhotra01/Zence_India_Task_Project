package com.main;

import com.main.dto.JwtResponse;
import com.main.dto.LoginRequest;
import com.main.dto.RegisterRequest;
import com.main.entity.Role;
import com.main.entity.User;
import com.main.repository.UserRepository;
import com.main.security.JwtService;
import com.main.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest(
                "om",
                "om@gmail.com",
                "password123"
        );

        loginRequest = new LoginRequest(
                "om",
                "password123"
        );

        user = User.builder()
                .id(1L)
                .username("om")
                .email("om@gmail.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void testRegisterUser() {

        when(userRepository.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String response = authService.register(registerRequest);

        assertEquals("User registered successfully.", response);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_WhenUsernameAlreadyExists() {

        when(userRepository.existsByUsername(registerRequest.username())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("Username already exists.", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_WhenEmailAlreadyExists() {

        when(userRepository.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("Email already exists.", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testLoginSuccess() {

        when(userRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        JwtResponse response = authService.login(loginRequest);

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("om", response.getUsername());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void testLogin_UserNotFound() {

        when(userRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Invalid username or password.", exception.getMessage());
    }

    @Test
    void testGenerateJwtToken() {

        when(userRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user))
                .thenReturn("sample-jwt-token");

        JwtResponse response = authService.login(loginRequest);

        assertNotNull(response.getToken());
        assertEquals("sample-jwt-token", response.getToken());
    }

}