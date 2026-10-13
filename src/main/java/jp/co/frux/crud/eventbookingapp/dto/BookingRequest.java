package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingRequest {
    @NotNull(message = "Event is required")
    @Min(value = 0, message = "Event is required")
    private Integer eventId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
