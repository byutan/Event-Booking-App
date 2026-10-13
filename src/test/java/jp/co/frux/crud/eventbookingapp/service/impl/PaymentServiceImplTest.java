package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.PaymentRequest;
import jp.co.frux.crud.eventbookingapp.dto.PaymentResponse;
import jp.co.frux.crud.eventbookingapp.entity.*;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.BookingRepository;
import jp.co.frux.crud.eventbookingapp.repository.PaymentRepository;
import jp.co.frux.crud.eventbookingapp.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    private PaymentRequest req;

    private User user;

    private Event event;

    private Booking booking;

    @BeforeEach
    public void setUp() {
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

        req = new PaymentRequest(
                1,
                "1234 5678 9012 3456",
                "12/25",
                "1234"
        );
    }

    @Test
    void createPayment_withNonExistBooking_shouldReturnNotFoundException() {
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());
        Exception res = assertThrows(BusinessValidationException.class, () -> paymentServiceImpl.createPayment(req));
        assertEquals("Booking not found or already paid", res.getMessage());
    }

    @Test
    void createPayment_withPaidBooking_shouldReturnConflictException() {
        booking = new Booking(
                2,
                50.0,
                "PAID",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user,
                event
        );
        booking.setId(1);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        Exception res = assertThrows(BusinessValidationException.class, () -> paymentServiceImpl.createPayment(req));
        assertEquals("Booking not found or already paid", res.getMessage());
    }

    @Test
    void createPayment_shouldReturnPaymentResponse() {
        booking = new Booking(
                2,
                50.0,
                "PENDING",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user,
                event
        );
        booking.setId(1);

        Payment payment = new Payment(
                50.0,
                "PAID",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                booking
        );

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(ticketRepository.existsByTicketCode(any(String.class))).thenReturn(false);
        PaymentResponse<PaymentResponse.PaymentData> res = paymentServiceImpl.createPayment(req);
        assertEquals("Payment successful", res.getMessage());
        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().paymentId());
        assertEquals(1, res.getData().bookingId());
        assertEquals(50.0, res.getData().amount());
        assertEquals("PAID", res.getData().status());
    }
}
