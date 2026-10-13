package jp.co.frux.crud.eventbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BookingResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public record bookingData(
            int bookingId,
            EventResponse.EventData event,
            int quantity,
            Double totalPrice
    ) {}
}
