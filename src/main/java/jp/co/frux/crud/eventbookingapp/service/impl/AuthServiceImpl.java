package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.RegisterRequest;
import jp.co.frux.crud.eventbookingapp.dto.RegisterResponse;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessConflictException;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import jp.co.frux.crud.eventbookingapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
