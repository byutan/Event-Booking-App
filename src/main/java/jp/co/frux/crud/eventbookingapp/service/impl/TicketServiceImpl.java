package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.TicketResponse;
import jp.co.frux.crud.eventbookingapp.entity.Booking;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.entity.Ticket;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import jp.co.frux.crud.eventbookingapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final UserRepository userRepository;

    @Override
    public TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>> getAllTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessValidationException("User not found", HttpStatus.NOT_FOUND));

        List<Booking> listBookings = user.getBookings();
        List<TicketResponse.TicketData<TicketResponse.EventData>> listTicketData = new ArrayList<>();

        for(Booking booking : listBookings) {
            List<Ticket> listTicket = booking.getTicket();
            for(Ticket ticket : listTicket) {
                TicketResponse.TicketData<TicketResponse.EventData> dataTicket = getEventDataTicketData(ticket);
                listTicketData.add(dataTicket);
            }
        }

        boolean isSuccess = true;
        String returnMessage = "Tickets fetched successfully";
        
        return TicketResponse.<List<TicketResponse.TicketData<TicketResponse.EventData>>>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(listTicketData)
                .build();
    }

    private static TicketResponse.@NonNull TicketData<TicketResponse.EventData> getEventDataTicketData(Ticket ticket) {
        Event event = ticket.getBooking().getEvent();
        TicketResponse.EventData dataEvent = new TicketResponse.EventData(
                event.getId(),
                event.getTitle(),
                event.getDateTime(),
                event.getLocation());
        return new TicketResponse.TicketData<>(
                ticket.getId(),
                ticket.getTicketCode(),
                ticket.getBooking().getId(),
                dataEvent,
                ticket.getStatus());
    }
}
