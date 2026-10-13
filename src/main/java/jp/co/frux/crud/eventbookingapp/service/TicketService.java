package jp.co.frux.crud.eventbookingapp.service;

import jp.co.frux.crud.eventbookingapp.dto.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>> getAllTickets();
}
