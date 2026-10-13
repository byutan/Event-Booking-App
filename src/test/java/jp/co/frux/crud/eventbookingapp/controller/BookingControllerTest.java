package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.BookingRequest;
import jp.co.frux.crud.eventbookingapp.dto.BookingResponse;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;


    private Event event;

    @BeforeEach
    public void setup() {
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
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void booking_shouldReturn201() throws Exception {
        EventResponse.EventData eventData = new EventResponse.EventData(
                event.getId(),
                event.getTitle(),
                event.getDateTime(),
                event.getLocation(),
                event.getPrice(),
                event.getDescription(),
                event.getImageUrl()
        );
        BookingRequest bookingRequest = new BookingRequest(0, 4);
        BookingResponse<BookingResponse.bookingData> res = BookingResponse.<BookingResponse.bookingData>builder()
                .success(true)
                .message("Booking successful")
                .data(new BookingResponse.bookingData(
                        0,
                        eventData,
                        4,
                        100.00
                ))
                .build();
        when(bookingService.createBooking(bookingRequest)).thenReturn(res);
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking successful"))
                .andExpect(jsonPath("$.data.bookingId").value(0))
                .andExpect(jsonPath("$.data.event.title").value("Art Exhibition"))
                .andExpect(jsonPath("$.data.event.dateTime").value("2027-05-12T10:00:00"))
                .andExpect(jsonPath("$.data.event.location").value("Modern Art Gallery, New York"))
                .andExpect(jsonPath("$.data.event.price").value(25.00))
                .andExpect(jsonPath("$.data.event.description").value("Lorem ipsum dolor sit amet..."))
                .andExpect(jsonPath("$.data.event.imageUrl").value("https://cdn.example.com/events/art_exhibition.jpg"))
                .andExpect(jsonPath("$.data.quantity").value(4))
                .andExpect(jsonPath("$.data.totalPrice").value(100.00));
    }

    @Test
    void booking_withNoToken_shouldReturn401() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void booking_withRoleUser_shouldReturn201() throws Exception {
        EventResponse.EventData eventData = new EventResponse.EventData(
                event.getId(),
                event.getTitle(),
                event.getDateTime(),
                event.getLocation(),
                event.getPrice(),
                event.getDescription(),
                event.getImageUrl()
        );
        BookingRequest bookingRequest = new BookingRequest(0, 4);
        BookingResponse<BookingResponse.bookingData> res = BookingResponse.<BookingResponse.bookingData>builder()
                .success(true)
                .message("Booking successful")
                .data(new BookingResponse.bookingData(
                        0,
                        eventData,
                        4,
                        100.00
                ))
                .build();
        when(bookingService.createBooking(bookingRequest)).thenReturn(res);
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking successful"))
                .andExpect(jsonPath("$.data.bookingId").value(0))
                .andExpect(jsonPath("$.data.event.title").value("Art Exhibition"))
                .andExpect(jsonPath("$.data.event.dateTime").value("2027-05-12T10:00:00"))
                .andExpect(jsonPath("$.data.event.location").value("Modern Art Gallery, New York"))
                .andExpect(jsonPath("$.data.event.price").value(25.00))
                .andExpect(jsonPath("$.data.event.description").value("Lorem ipsum dolor sit amet..."))
                .andExpect(jsonPath("$.data.event.imageUrl").value("https://cdn.example.com/events/art_exhibition.jpg"))
                .andExpect(jsonPath("$.data.quantity").value(4))
                .andExpect(jsonPath("$.data.totalPrice").value(100.00));
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void booking_withWrongInput_shouldReturn422() throws Exception {
        BookingRequest bookingRequest = new BookingRequest(0, -1);
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isUnprocessableContent());
    }

    @WithMockUser(username = "manager@example.com", roles = "MANAGER")
    @Test
    void booking_withManagerRole_shouldReturn403() throws Exception {
        BookingRequest request = new BookingRequest(1, 2);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
