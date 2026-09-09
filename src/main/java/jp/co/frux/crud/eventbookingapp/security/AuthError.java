package jp.co.frux.crud.eventbookingapp.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthError {
    private boolean success;
    private String message;
}
