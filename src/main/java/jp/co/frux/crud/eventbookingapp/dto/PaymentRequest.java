package jp.co.frux.crud.eventbookingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
    @NotNull(message = "Booking is required")
    @Positive(message = "Booking not found")
    private Integer bookingId;

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^(\\d{4}[- ]?){3}\\d{4}$|^\\d{16}$", message = "Invalid card number")
    private String cardNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid expiry format")
    private String expiry;

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV")
    private String cvv;
}
