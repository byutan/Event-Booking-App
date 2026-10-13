package jp.co.frux.crud.eventbookingapp.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessValidationException extends BusinessException {
    private final HttpStatus status;
    public BusinessValidationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
