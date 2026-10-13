package jp.co.frux.crud.eventbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String message;
    private UserData data;

    public record UserData(
            int userId,
            String fullName,
            String email
    ) {}
}

