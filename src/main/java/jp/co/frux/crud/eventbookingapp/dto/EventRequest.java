package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Date & Time is required")
    @Future(message = "Date & Time must be in the future")
    private LocalDateTime dateTime;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Price is required")
    @DecimalMin(message = "Price must be >= 0", value = "0")
    private Double price;
    
    @Length(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @Min(value = -90, message = "Latitude must be between -180 and 180")
    @Max(value = 180, message = "Latitude must be between -180 and 180")
    @NotNull(message = "Longitude is required is required")
    private Double longitude;


    @URL(message = "Invalid image URL")
    @Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg|gif|webp|svg))(?:\\?.*)?$",
            message = "Invalid image URL")
    private String imageUrl;
}
