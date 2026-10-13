package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventFilterRequest {
    @Pattern(regexp = "^(popular|upcoming|nearby)$", message = "Invalid type value")
    private String type;

    private String search;

    @Min(value = 1, message = "Page must be a positive integer")
    private Integer page = 1;

    @Min(value = 1, message = "Size must be a positive integer")
    private Integer size = 10;

    private Double latitude;
    private Double longitude;
}
