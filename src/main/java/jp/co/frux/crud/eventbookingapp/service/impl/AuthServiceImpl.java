package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.LoginRequest;
import jp.co.frux.crud.eventbookingapp.dto.LoginResponse;
import jp.co.frux.crud.eventbookingapp.dto.RegisterRequest;
import jp.co.frux.crud.eventbookingapp.dto.RegisterResponse;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessConflictException;
import jp.co.frux.crud.eventbookingapp.exception.BusinessException;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import jp.co.frux.crud.eventbookingapp.security.JwtService;
import jp.co.frux.crud.eventbookingapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest req) throws BusinessConflictException {
        String email = req.getEmail();

        if(userRepository.existsByEmail(email)) {
            String message = "Email is already registered";

            throw new BusinessConflictException(message);
        }

        String fullName = req.getFullName();

        String password = req.getPassword();
        String hashedPassword = passwordEncoder.encode(password);

        LocalDateTime createdAt = LocalDateTime.now();
        String defaultRole = "USER";

        User savedUser = userRepository.save(new
                User(fullName, email, hashedPassword, null, defaultRole, createdAt));

        return RegisterResponse.builder()
                .success(true)
                .message("Registration successful")
                .data(new RegisterResponse.UserData(
                        savedUser.getId(),
                        savedUser.getFullName(),
                        savedUser.getEmail()
                ))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest req) throws BusinessValidationException {
        String email = req.getEmail();
        String message = "Invalid email or password";
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        User loadedUser = userRepository.findByEmail(email).orElseThrow(() -> new BusinessValidationException(message, status));

        if (!passwordEncoder.matches(req.getPassword(), loadedUser.getPassword())) {
            throw new BusinessValidationException(message, status);
        }

        String token = jwtService.generateToken(loadedUser);
        Instant exp = jwtService.getExpiration(token);

        return LoginResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .data(new LoginResponse.UserToken(token, exp))
                    .build();
    }
}
