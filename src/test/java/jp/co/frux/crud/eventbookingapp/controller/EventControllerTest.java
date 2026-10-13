package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.EventRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;
import jp.co.frux.crud.eventbookingapp.service.EventService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void createEvent_shouldReturn201() throws Exception {
        EventRequest req = new EventRequest(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                25.00,
                "Lorem ipsum dolor sit amet...",
                86.660172,
                10.762622,
                "https://cdn.example.com/events/art_exhibition.jpg"

        );

        EventResponse<EventResponse.EventData> res = EventResponse.<EventResponse.EventData>builder()
                .success(true)
                .message("Event created successfully")
                .data(new EventResponse.EventData(
                        3,
                        "Art Exhibition",
                        LocalDateTime.parse("2027-05-12T10:00:00"),
                        "Modern Art Gallery, New York",
                        25.00,
                        "Lorem ipsum dolor sit amet...",
                        "https://cdn.example.com/events/art_exhibition.jpg"
                ))
                .build();

        when(eventService.createEvent(any(EventRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Event created successfully"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.title").value("Art Exhibition"))
                .andExpect(jsonPath("$.data.dateTime").value("2027-05-12T10:00:00"))
                .andExpect(jsonPath("$.data.location").value("Modern Art Gallery, New York"))
                .andExpect(jsonPath("$.data.price").value(25.00))
                .andExpect(jsonPath("$.data.description").value("Lorem ipsum dolor sit amet..."))
                .andExpect(jsonPath("$.data.imageUrl")
                        .value("https://cdn.example.com/events/art_exhibition.jpg"));
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void createEvent_withUserRole_shouldReturn403() throws Exception {
        EventRequest req = new EventRequest(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                25.00,
                "Lorem ipsum dolor sit amet...",
                86.660172,
                10.762622,
                "https://cdn.example.com/events/art_exhibition.jpg"
        );

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());

        verify(eventService, never()).createEvent(any(EventRequest.class));
    }

    @Test
    void createEvent_withoutLogin_shouldReturn403() throws Exception {
        EventRequest req = new EventRequest(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                25.00,
                "Lorem ipsum dolor sit amet...",
                106.660172,
                10.762622,
                "https://cdn.example.com/events/art_exhibition.jpg"
        );

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void createEvent_withInvalidRequest_shouldReturn422() throws Exception {
        EventRequest req = new EventRequest(
                "",
                LocalDateTime.of(2022, 5, 12, 10, 0),
                "",
                0.00,
                "",
                0.00,
                0.00,
                ""
        );

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.length()").value(4));
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void deleteEvent_shouldReturn204() throws Exception {
        doNothing().when(eventService).deleteEvent(1);

        mockMvc.perform(delete("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void deleteEvent_withUserRole_shouldReturn403() throws Exception {
        doNothing().when(eventService).deleteEvent(1);

        mockMvc.perform(delete("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteEvent_withoutLogin_shouldReturn401() throws Exception {
        mockMvc.perform(delete("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void updateEvent_withUserRole_shouldReturn403() throws Exception {
        EventRequest req = new EventRequest(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                25.00,
                "Lorem ipsum dolor sit amet...",
                86.660172,
                10.762622,
                "https://cdn.example.com/events/art_exhibition.jpg"
        );

        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateEvent_withoutLogin_shouldReturn401() throws Exception {
        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void updateEvent_shouldReturn200() throws Exception {
        EventRequest req = new EventRequest(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                25.00,
                "Lorem ipsum dolor sit amet...",
                86.660172,
                10.762622,
                "https://cdn.example.com/events/art_exhibition.jpg"
        );
        EventResponse<EventResponse.EventData> res = EventResponse.<EventResponse.EventData>builder()
                .success(true)
                .message("Event updated successfully")
                .data(new EventResponse.EventData(
                        3,
                        "Art Exhibition",
                        LocalDateTime.parse("2027-05-12T10:00:00"),
                        "Modern Art Gallery, New York",
                        25.00,
                        "Lorem ipsum dolor sit amet...",
                        "https://cdn.example.com/events/art_exhibition.jpg"
                ))
                .build();
        when(eventService.updateEvent(eq(3), any(EventRequest.class))).thenReturn(res);

        mockMvc.perform(put("/api/events/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Event updated successfully"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.title").value("Art Exhibition"))
                .andExpect(jsonPath("$.data.dateTime").value("2027-05-12T10:00:00"))
                .andExpect(jsonPath("$.data.location").value("Modern Art Gallery, New York"))
                .andExpect(jsonPath("$.data.price").value(25.00))
                .andExpect(jsonPath("$.data.description").value("Lorem ipsum dolor sit amet..."))
                .andExpect(jsonPath("$.data.imageUrl")
                        .value("https://cdn.example.com/events/art_exhibition.jpg"));
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void updateEvent_withInvalidInput_shouldReturn422() throws Exception {
        EventRequest req = new EventRequest(
                "",
                LocalDateTime.of(2025, 5, 12, 10, 0),
                "",
                0.00,
                "",
                86.660172,
                10.762622,
                ""
        );

        mockMvc.perform(put("/api/events/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.length()").value(4))
                .andExpect(jsonPath("$.errors[?(@.field == 'title')]")
                        .isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.field == 'dateTime')]")
                        .isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.field == 'location')]")
                        .isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.field == 'imageUrl')]")
                        .isNotEmpty());

        verify(eventService, never())
                .updateEvent(anyInt(), any(EventRequest.class));
    }

    @Test
    void getEventWithId_withoutLogin_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "MANAGER")
    @Test
    void getEventWithId_withoutPermission_shouldReturn403() throws Exception {
        mockMvc.perform(get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "jane@example.com", roles = "MANAGER")
    @Test
    void getEvent_withoutPermission_shouldReturn403() throws Exception {
        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getEvent_withoutLogin_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void getEvent_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
