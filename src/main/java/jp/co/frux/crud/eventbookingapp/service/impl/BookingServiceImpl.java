package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.BookingRequest;
import jp.co.frux.crud.eventbookingapp.dto.BookingResponse;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;
import jp.co.frux.crud.eventbookingapp.entity.Booking;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.BookingRepository;
import jp.co.frux.crud.eventbookingapp.repository.EventRepository;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import jp.co.frux.crud.eventbookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponse<BookingResponse.bookingData> createBooking(BookingRequest bookingRequest) {
        int eventId = bookingRequest.getEventId();
        String message = "Event not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new BusinessValidationException(message, status));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessValidationException("User not found", HttpStatus.NOT_FOUND));

        int quantity = bookingRequest.getQuantity();
        LocalDateTime createdAt = LocalDateTime.now();
        Double totalPrice = event.getPrice() * quantity;

        Booking booking = new Booking(quantity, totalPrice, "PENDING", createdAt, user, event);
        Booking savedBooking = bookingRepository.save(booking);

        event.getBookings().add(savedBooking);

        boolean isSuccess = true;
        String returnMessage = "Booking successful";

        EventResponse.EventData eventData = getEventData(event, eventId);

        return BookingResponse.<BookingResponse.bookingData>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(new BookingResponse.bookingData(
                        savedBooking.getId(),
                        eventData,
                        quantity,
                        totalPrice
                ))
                .build();
    }

    private static EventResponse.@NonNull EventData getEventData(Event event, int eventId) {
        String title = event.getTitle();
        LocalDateTime dateTime = event.getDateTime();
        String location = event.getLocation();
        Double price = event.getPrice();
        String description = event.getDescription();
        String imageUrl = event.getImageUrl();

        return new EventResponse.EventData(
                eventId,
                title,
                dateTime,
                location,
                price,
                description,
                imageUrl);
    }
}
