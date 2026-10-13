package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.TicketResponse;
import jp.co.frux.crud.eventbookingapp.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
public class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void createPayment_shouldReturn200() throws Exception {
        TicketResponse.EventData eventData = new TicketResponse.EventData(
                1,
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York"
        );
        TicketResponse.TicketData<TicketResponse.EventData> ticketData = new TicketResponse.TicketData<>(
                1,
                "EVT-3-BRF4Z",
                3,
                eventData,
                "PAID"
        );
        TicketResponse<List<TicketResponse.TicketData<TicketResponse.EventData>>> ticketResponse =
                new TicketResponse<>(
                        true,
                        "Tickets fetched successfully",
                        List.of(ticketData)
                );
        when(ticketService.getAllTickets()).thenReturn(ticketResponse);
        mockMvc.perform(get("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tickets fetched successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].ticketId").value(1))
                .andExpect(jsonPath("$.data[0].ticketCode").value("EVT-3-BRF4Z"))
                .andExpect(jsonPath("$.data[0].bookingId").value(3))
                .andExpect(jsonPath("$.data[0].event.eventId").value(1))
                .andExpect(jsonPath("$.data[0].event.title").value("Art Exhibition"))
                .andExpect(jsonPath("$.data[0].event.dateTime").value("2027-05-12T10:00:00"))
                .andExpect(jsonPath("$.data[0].event.location").value("Modern Art Gallery, New York"))
                .andExpect(jsonPath("$.data[0].status").value("PAID"));
    }

    @Test
    void createPayment_withoutLogin_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "MANAGER")
    @Test
    void createPayment_withoutPermission_shouldReturn403() throws Exception {
        mockMvc.perform(get("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
