package jp.co.frux.crud.eventbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TicketResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public record TicketData<T> (
            int ticketId,
            String ticketCode,
            int bookingId,
            T event,
            String status
    ) {}

    public record EventData (
            int eventId,
            String title,
            LocalDateTime dateTime,
            String location
    ) {}
}
