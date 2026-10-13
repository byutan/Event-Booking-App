package jp.co.frux.crud.eventbookingapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.frux.crud.eventbookingapp.security.AuthError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest req) {
            HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
            List<ValidationError.ErrorDetail> errors = e.getBindingResult().getFieldErrors().stream()
                    .map(err -> new ValidationError.ErrorDetail(
                            err.getField(),
                            err.getDefaultMessage()
                    )).collect(Collectors.toList());


            boolean isSuccess = false;
            String message = "Validation failed";

            return ResponseEntity.status(status).body(new ValidationError(
                    isSuccess,
                    message,
                    errors
            ));
    }

    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<APIError> handleBusinessConflictException(
            BusinessConflictException e, HttpServletRequest req) {
        HttpStatus status = HttpStatus.CONFLICT;

        boolean isSuccess = false;
        String message = e.getMessage();

        return ResponseEntity.status(status).body(new APIError(
                isSuccess,
                message,
                null
        ));
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<AuthError> handleBusinessValidationException(
            BusinessValidationException e, HttpServletRequest req) {
        boolean isSuccess = false;
        HttpStatus status = e.getStatus();
        String message = e.getMessage();

        return ResponseEntity.status(status).body(new AuthError(
                isSuccess,
                message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationError> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest req) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;

        List<ValidationError.ErrorDetail> errors = List.of(
                new ValidationError.ErrorDetail(
                        "eventReminder",
                        "Must be true or false"
                )
        );

        boolean isSuccess = false;
        String message = "Validation failed";

        return ResponseEntity.status(status).body(new ValidationError(
                isSuccess,
                message,
                errors
        ));
    }
}
