package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.LoginRequest;
import jp.co.frux.crud.eventbookingapp.dto.LoginResponse;
import jp.co.frux.crud.eventbookingapp.dto.RegisterRequest;
import jp.co.frux.crud.eventbookingapp.dto.RegisterResponse;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessConflictException;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import jp.co.frux.crud.eventbookingapp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;


    @Test
    void register_shouldReturnSavedUser() {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "password123");

        when(passwordEncoder.encode("password123")).thenReturn("password123");
        when(userRepository.save(any(User.class))).thenReturn(new User(
                1,
                "Jane Doe",
                "jane@example.com",
                "password123",
                null,
                "USER",
                LocalDateTime.now()));

        RegisterResponse res = authServiceImpl.register(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(1, res.getData().userId());
        assertEquals(req.getFullName(), res.getData().fullName());
        assertEquals(req.getEmail(), res.getData().email());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_withRegisteredEmail_shouldReturnConflictException() {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "password123");

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        BusinessConflictException exc = assertThrows(BusinessConflictException.class, () -> authServiceImpl.register(req));

        assertEquals("Email is already registered", exc.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void login_shouldReturnTokenAndExpirationTime() {
        LoginRequest req = new LoginRequest(
                "jane@example.com",
                "password123");
        Instant exp = Instant.now().plusSeconds(3600);
        User user = new User(
                "Jane Doe",
                "jane@example.com",
                "password123",
                null,
                "USER",
                LocalDateTime.now());

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder
                .matches("password123", "password123"))
                .thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");
        when(jwtService.getExpiration("token")).thenReturn(exp);

        LoginResponse res = authServiceImpl.login(req);

        assertTrue(res.isSuccess());
        assertEquals("Login successful", res.getMessage());
        assertNotNull(res.getData());
        assertEquals(
                "token",
                res.getData().token()
        );
        assertEquals(
                exp,
                res.getData().expire()
        );

        verify(jwtService).generateToken(user);
        verify(jwtService).getExpiration("token");
    }

    @Test
    void login_withNonExistedEmail_shouldReturnValidationException() {
        LoginRequest req = new LoginRequest(
                "user@example.com",
                "password123"
        );

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        Exception exc = assertThrows(BusinessValidationException.class, () -> authServiceImpl.login(req));

        assertEquals("Invalid email or password", exc.getMessage());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_withIncorrectPassword_shouldReturnValidationException() {
        LoginRequest req = new LoginRequest(
                "jane@example.com",
                "password12345"
        );

        User user = new User(
                "Jane Doe",
                "jane@example.com",
                "password123",
                null,
                "USER",
                LocalDateTime.now());

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder
                .matches("password12345", "password123"))
                .thenReturn(false);

        BusinessValidationException exc = assertThrows(BusinessValidationException.class, () -> authServiceImpl.login(req));

        assertEquals("Invalid email or password", exc.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exc.getStatus());
        verify(jwtService, never()).generateToken(any());
    }
}
