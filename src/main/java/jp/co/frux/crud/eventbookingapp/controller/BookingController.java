package jp.co.frux.crud.eventbookingapp.controller;

import jakarta.validation.Valid;
import jp.co.frux.crud.eventbookingapp.dto.BookingRequest;
import jp.co.frux.crud.eventbookingapp.dto.BookingResponse;
import jp.co.frux.crud.eventbookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    ResponseEntity<BookingResponse<BookingResponse.bookingData>> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse<BookingResponse.bookingData> res = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
