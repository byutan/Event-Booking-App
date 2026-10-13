package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.TicketResponse;
import jp.co.frux.crud.eventbookingapp.entity.Ticket;
import jp.co.frux.crud.eventbookingapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>>> getAllTickets() {
        TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>> res = ticketService.getAllTickets();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
