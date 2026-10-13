package jp.co.frux.crud.eventbookingapp.controller;

import jakarta.validation.Valid;
import jp.co.frux.crud.eventbookingapp.dto.EventRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventFilterRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;
import jp.co.frux.crud.eventbookingapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<EventResponse<EventResponse.EventData>> createEvent(@Valid @RequestBody EventRequest req) {
        EventResponse<EventResponse.EventData> res = eventService.createEvent(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<EventResponse<EventResponse.EventData>> updateEvent(@PathVariable Integer id, @Valid @RequestBody EventRequest req) {
        EventResponse<EventResponse.EventData> res = eventService.updateEvent(id, req);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    ResponseEntity<EventResponse<List<EventResponse.EventData>>> getEvents(@Valid @ModelAttribute EventFilterRequest filter) {
        EventResponse<List<EventResponse.EventData>> res = eventService.getEvents(filter);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<EventResponse<EventResponse.EventData>> getEventById(@PathVariable Integer id) {
        EventResponse<EventResponse.EventData> res = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
