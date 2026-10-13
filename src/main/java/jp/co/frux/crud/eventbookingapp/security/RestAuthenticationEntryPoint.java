package jp.co.frux.crud.eventbookingapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException) throws IOException {
        String errorMessage = (String) request.getAttribute("auth_error");
        if (errorMessage == null) {
            String requestUri = request.getRequestURI();

            if (requestUri.startsWith("/api/tickets")) {
                errorMessage = "Unauthorized – Please login to access this source";
            }  else if (requestUri.startsWith("/api/events")) {
            errorMessage = "Unauthorized - Please login";
            } else {
            errorMessage = "Missing access token";
            }
        }

        AuthError body = new AuthError(false, errorMessage);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
