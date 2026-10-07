package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EventRequestFilter {
    @Pattern(regexp = "^(popular|upcoming|nearby)$")
    private String type;

    private String search;

    @Min(value = 1)
    private Integer page = 1;

    @Min(value = 1)
    private Integer size = 10;
}
