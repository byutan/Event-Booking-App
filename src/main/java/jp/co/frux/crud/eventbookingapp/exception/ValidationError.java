package jp.co.frux.crud.eventbookingapp.exception;

import java.util.List;

public class ValidationError extends APIError {
    public ValidationError(boolean success, String message, List<ErrorDetail> errors) {
        super(success, message, errors);
    }

    public record ErrorDetail(String field, String message) {}
}
