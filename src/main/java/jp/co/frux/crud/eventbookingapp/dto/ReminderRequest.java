package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReminderRequest {
    @NotNull(message = "Event reminder must be true or false")
    private Boolean eventReminder;
}
