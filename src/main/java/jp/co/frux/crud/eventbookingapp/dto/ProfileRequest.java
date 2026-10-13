package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @URL(message = "Invalid image URL")
    @Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg|gif|webp|svg))(?:\\?.*)?$",
            message = "Invalid image URL")
    private String avatar;
}
