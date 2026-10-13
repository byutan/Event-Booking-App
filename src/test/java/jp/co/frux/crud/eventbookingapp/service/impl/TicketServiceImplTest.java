package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.TicketResponse;
import jp.co.frux.crud.eventbookingapp.entity.Booking;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.entity.Ticket;
import jp.co.frux.crud.eventbookingapp.entity.User;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketServiceImpl;

    private User user;

    private Booking booking;

    private Ticket ticket;
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

        Event event = new Event(
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
                1,
                25.0,
                "PAID",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user,
                event
        );

        ticket = new Ticket(
                "EVT-3-BRF4Z",
                "PAID",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                booking
        );
    }

    @Test
    void createPayment_shouldReturnListOfTickets() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        booking.setTicket(List.of(ticket));
        user.setBookings(List.of(booking));
        when(userRepository.findByEmail("janedoe@email.com")).thenReturn(Optional.of(user));
        TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>> res = ticketServiceImpl.getAllTickets();
        assertTrue(res.isSuccess());
        assertEquals("Tickets fetched successfully", res.getMessage());
        assertEquals(1, res.getData().size());
    }

    @Test
    void createPayment_shouldReturnEmptyList() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        booking.setTicket(List.of());
        user.setBookings(List.of(booking));
        when(userRepository.findByEmail("janedoe@email.com")).thenReturn(Optional.of(user));
        TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>> res = ticketServiceImpl.getAllTickets();
        assertTrue(res.isSuccess());
        assertEquals("Tickets fetched successfully", res.getMessage());
        assertEquals(0, res.getData().size());
    }
}
