package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.LoginRequest;
import jp.co.frux.crud.eventbookingapp.dto.LoginResponse;
import jp.co.frux.crud.eventbookingapp.dto.RegisterResponse;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import org.springframework.http.HttpStatus;
import tools.jackson.databind.ObjectMapper;
import jp.co.frux.crud.eventbookingapp.dto.RegisterRequest;
import jp.co.frux.crud.eventbookingapp.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_shouldReturn201() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "password123");

        RegisterResponse res = RegisterResponse.builder()
                .success(true)
                .message("Registration successful")
                .data(new RegisterResponse.UserData(
                        1,
                        "Jane Doe",
                        "jane@example.com"
                ))
                .build();

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(res);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.data.email").value("jane@example.com"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void register_withEmptyReq_shouldReturn422() throws Exception {
        RegisterRequest req = new RegisterRequest("", "", "");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(3));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void register_withInvalidEmail_shouldReturn422() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "janeexample.com",
                "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(1));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void register_withCharactersOnlyPassword_shouldReturn422() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "password");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(1));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void register_withNumbersOnlyPassword_shouldReturn422() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(1));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void register_withLessThan8CharacterPassword_shouldReturn422() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe",
                "jane@example.com",
                "pass123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(1));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void login_shouldReturn200() throws Exception {
        LoginRequest req = new LoginRequest(
                "jane@example.com",
                "password123");
        Instant exp = Instant.now().plusSeconds(3600);
        LoginResponse response = LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .data(new LoginResponse.UserToken(
                        "token",
                        exp
                )).build();

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("token"))
                .andExpect(jsonPath("$.data.expire").value(exp.toString()));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void login_withEmptyReq_shouldReturn422() throws Exception {
        LoginRequest req = new LoginRequest(
                "",
                "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(2));

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    void login_withInvalidEmail_shouldReturn422() throws Exception {
        LoginRequest req = new LoginRequest(
                "janeexample.com",
                "password123");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors.length()").value(1));

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        LoginRequest req = new LoginRequest(
                "jane@example.com",
                "password123");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BusinessValidationException(
                        "Invalid email or password",
                        HttpStatus.UNAUTHORIZED
                ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Invalid email or password"));

        verify(authService).login(any(LoginRequest.class));
    }
}
