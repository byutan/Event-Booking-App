package jp.co.frux.crud.eventbookingapp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public record EventData (
            int id,
            String title,
            LocalDateTime dateTime,
            String location,
            Double price,
            String description,
            String imageUrl
    ) {}
}
