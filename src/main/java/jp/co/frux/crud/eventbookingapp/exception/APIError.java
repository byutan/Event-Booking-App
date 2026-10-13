package jp.co.frux.crud.eventbookingapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class APIError {
    private boolean success;
    private String message;
    private List<?> errors;
}
