package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.RegisterRequest;
import jp.co.frux.crud.eventbookingapp.dto.RegisterResponse;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessConflictException;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
                "Jane Doe",
                "jane@example.com",
                "password123",
                null,
                "USER",
                LocalDateTime.now()));
        RegisterResponse res = authServiceImpl.register(req);
        assertEquals(req.getFullName(), res.getData().fullName());
    }

    @Test
    void register_withRegisteredEmail_shouldReturnConflictException() {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "password123");
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);
        Exception exc = assertThrows(BusinessConflictException.class, () -> authServiceImpl.register(req));
        assertEquals("Email is already registered", exc.getMessage());
    }
}
