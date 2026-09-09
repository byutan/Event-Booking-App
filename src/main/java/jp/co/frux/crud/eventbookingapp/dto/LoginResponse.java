package jp.co.frux.crud.eventbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private UserToken data;

    public record UserToken(
            String token,
            Instant expire
    ) {}
}
