package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.BookingRequest;
import jp.co.frux.crud.eventbookingapp.dto.BookingResponse;
import jp.co.frux.crud.eventbookingapp.entity.Booking;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.BookingRepository;
import jp.co.frux.crud.eventbookingapp.repository.EventRepository;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private BookingRequest req;

    private User user;

    private Event event;

    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User(
                "Jane",
                "janedoe@email.com",
                "Kh@0104",
                "avatar",
                "USER",
                LocalDateTime.of(2027, 5, 12, 10, 0)
        );

        event = new Event(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                00.00,
                00.00,
                25.00,
                "Lorem ipsum dolor sit amet...",
                "https://cdn.example.com/events/art_exhibition.jpg",
                LocalDateTime.now()
        );

        booking = new Booking(
                2,
                50.0,
                "PENDING",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user,
                event
        );

        req = new BookingRequest(0, 4);
    }

    @Test
    void createBooking_withNonExistEvent_shouldReturnNotFound() {
        when(eventRepository.findById(0)).thenReturn(Optional.empty());
        Exception res = assertThrows(BusinessValidationException.class, () -> bookingServiceImpl.createBooking(req));
        assertEquals("Event not found", res.getMessage());
    }

    @Test
    void createBooking_shouldReturnBooking() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        when(eventRepository.findById(0)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail("janedoe@email.com")).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse<BookingResponse.bookingData> res = bookingServiceImpl.createBooking(req);
        assertTrue(res.isSuccess());
        assertEquals("Booking successful", res.getMessage());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().bookingId());
        assertEquals("Art Exhibition", res.getData().event().title());
        assertEquals(
                LocalDateTime.of(2027, 5, 12, 10, 0),
                res.getData().event().dateTime()
        );
        assertEquals("Modern Art Gallery, New York", res.getData().event().location());
        assertEquals(25.00, res.getData().event().price());
        assertEquals("Lorem ipsum dolor sit amet...", res.getData().event().description());
        assertEquals("https://cdn.example.com/events/art_exhibition.jpg", res.getData().event().imageUrl());
        assertEquals(4, res.getData().quantity());
        assertEquals(100.00, res.getData().totalPrice());
    }
}
