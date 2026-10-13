package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.EventFilterRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    private EventRequest req;

    private Event event;

    @BeforeEach
    void setUp() {
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

        req = new EventRequest(
                "Art Exhibition",
                LocalDateTime.of(2027, 5, 12, 10, 0),
                "Modern Art Gallery, New York",
                25.0,
                "Lorem ipsum dolor sit amet...",
                106.660172,
                10.762622,
                "https://cdn.example.com/events/art_exhibition.jpg"
        );
    }

    @Test
    void createEvent_shouldReturnEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponse<EventResponse.EventData> res = eventServiceImpl.createEvent(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().id());
        assertEquals("Art Exhibition", res.getData().title());
        assertEquals("Modern Art Gallery, New York", res.getData().location());
        assertEquals(LocalDateTime.of(2027, 5, 12, 10, 0), res.getData().dateTime());
        assertEquals("Lorem ipsum dolor sit amet...", res.getData().description());
        assertEquals(25.00, res.getData().price());
        assertEquals("https://cdn.example.com/events/art_exhibition.jpg", res.getData().imageUrl());

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void deleteEvent_shouldDeleteOnce() {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(any(Event.class));
        eventServiceImpl.deleteEvent(1);
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void deleteEvent_withWrongId_shouldThrowException() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());
        Exception res = assertThrows(BusinessValidationException.class, () -> eventServiceImpl.deleteEvent(1));
        assertEquals("Event not found", res.getMessage());
    }

    @Test
    void updateEvent_withWrongId_shouldThrowException() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());
        Exception res = assertThrows(BusinessValidationException.class, () -> eventServiceImpl.updateEvent(1, req));
        assertEquals("Event not found", res.getMessage());
    }

    @Test
    void updateEvent_shouldReturnUpdatedEvent() {
        when(eventRepository.findById(0)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        EventResponse<EventResponse.EventData> res = eventServiceImpl.updateEvent(0,req);
        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().id());
        assertEquals("Art Exhibition", res.getData().title());
        assertEquals("Modern Art Gallery, New York", res.getData().location());
        assertEquals(LocalDateTime.of(2027, 5, 12, 10, 0), res.getData().dateTime());
        assertEquals("Lorem ipsum dolor sit amet...", res.getData().description());
        assertEquals(25.00, res.getData().price());
        assertEquals("https://cdn.example.com/events/art_exhibition.jpg", res.getData().imageUrl());

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void getEvent_withId_shouldReturnEventById() {
        when(eventRepository.findById(0)).thenReturn(Optional.of(event));

        EventResponse<EventResponse.EventData> res = eventServiceImpl.getEventById(0);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().id());
        assertEquals("Art Exhibition", res.getData().title());
        assertEquals("Modern Art Gallery, New York", res.getData().location());
        assertEquals(LocalDateTime.of(2027, 5, 12, 10, 0), res.getData().dateTime());
        assertEquals("Lorem ipsum dolor sit amet...", res.getData().description());
        assertEquals(25.00, res.getData().price());
        assertEquals("https://cdn.example.com/events/art_exhibition.jpg", res.getData().imageUrl());
    }

    @Test
    void getEvent_withWrongId_shouldThrowException() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());
        Exception res = assertThrows(BusinessValidationException.class, () -> eventServiceImpl.getEventById(1));
        assertEquals("Event not found", res.getMessage());
    }

    @Test
    void getEvents_withUpcomingType_shouldReturnEvents() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setType("upcoming");

        when(eventRepository.findUpcomingEvents(any(Pageable.class))).thenReturn(List.of(event));

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);

        assertTrue(res.isSuccess());
        assertEquals("Events fetched successfully", res.getMessage());
        assertNotNull(res.getData());
        assertEquals(1, res.getData().size());

        assertEquals("Art Exhibition", res.getData().getFirst().title());
        assertEquals("Modern Art Gallery, New York", res.getData().getFirst().location());

        verify(eventRepository, times(1)).findUpcomingEvents(any(Pageable.class));
        verify(eventRepository, never()).findPopularEvents(any(Pageable.class));
        verify(eventRepository, never()).findAllEvents(any(), any(Pageable.class));
    }

    @Test
    void getEvents_withUpcomingType_shouldReturnEmptyList() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setType("upcoming");

        when(eventRepository.findUpcomingEvents(any(Pageable.class))).thenReturn(List.of());

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);

        assertTrue(res.isSuccess());
        assertEquals("Events fetched successfully", res.getMessage());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().size());

        verify(eventRepository, times(1)).findUpcomingEvents(any(Pageable.class));
        verify(eventRepository, never()).findPopularEvents(any(Pageable.class));
        verify(eventRepository, never()).findAllEvents(any(), any(Pageable.class));
    }

    @Test
    void getEvents_withPopularType_shouldReturnEvents() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setType("popular");

        when(eventRepository.findPopularEvents(any(Pageable.class))).thenReturn(List.of(event));

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(1, res.getData().size());

        assertEquals("Art Exhibition", res.getData().getFirst().title());

        verify(eventRepository, times(1)).findPopularEvents(any(Pageable.class));
        verify(eventRepository, never()).findUpcomingEvents(any(Pageable.class));
        verify(eventRepository, never()).findAllEvents(any(), any(Pageable.class));
    }

    @Test
    void getEvents_withPopularType_shouldReturnEmptyList() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setType("popular");

        when(eventRepository.findPopularEvents(any(Pageable.class))).thenReturn(List.of());

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);

        assertTrue(res.isSuccess());
        assertEquals("Events fetched successfully", res.getMessage());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().size());

        verify(eventRepository, times(1)).findPopularEvents(any(Pageable.class));
        verify(eventRepository, never()).findNearbyEvents(any(Double.class),any(Double.class),any(Pageable.class));
        verify(eventRepository, never()).findAllEvents(any(), any(Pageable.class));
    }

    @Test
    void getEvents_withNearByType_shouldReturnEmptyList() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setLatitude(90.0);
        filter.setLongitude(90.0);
        filter.setType("nearby");

        when(eventRepository.findNearbyEvents(any(Double.class),any(Double.class),any(Pageable.class))).thenReturn(List.of());

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);

        assertTrue(res.isSuccess());
        assertEquals("Events fetched successfully", res.getMessage());
        assertNotNull(res.getData());
        assertEquals(0, res.getData().size());

        verify(eventRepository, times(1)).findNearbyEvents(any(Double.class),any(Double.class), any(Pageable.class));
        verify(eventRepository, never()).findPopularEvents(any(Pageable.class));
        verify(eventRepository, never()).findAllEvents(any(), any(Pageable.class));
    }
    @Test
    void getEvents_withNearbyType_shouldReturnEvents() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setType("nearby");
        filter.setLatitude(40.7128);
        filter.setLongitude(-74.0060);

        when(eventRepository.findNearbyEvents(
                eq(40.7128),
                eq(-74.0060),
                any(Pageable.class)
        )).thenReturn(List.of(event));

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals(1, res.getData().size());

        assertEquals("Art Exhibition", res.getData().getFirst().title());

        verify(eventRepository, times(1)).findNearbyEvents(
                eq(40.7128),
                eq(-74.0060),
                any(Pageable.class)
        );

        verify(eventRepository, never()).findUpcomingEvents(any(Pageable.class));
        verify(eventRepository, never()).findPopularEvents(any(Pageable.class));
    }

    @Test
    void getEvents_withSearch_shouldReturnEvents() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setSearch("Art Exhibition");

        when(eventRepository.findAllEvents(
                eq(filter.getSearch()),
                any(Pageable.class)
        )).thenReturn(List.of(event));
        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);
        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals("Art Exhibition", res.getData().getFirst().title());

        verify(eventRepository, times(1))
                .findAllEvents(eq(filter.getSearch()), any(Pageable.class));
        verify(eventRepository, never()).findUpcomingEvents(any(Pageable.class));
        verify(eventRepository, never()).findPopularEvents(any(Pageable.class));
    }

    @Test
    void getEvents_withNonExistSearch_shouldReturnEvents() {
        EventFilterRequest filter = new EventFilterRequest();
        filter.setPage(1);
        filter.setSize(10);
        filter.setSearch("Music Festival");

        when(eventRepository.findAllEvents(
                eq(filter.getSearch()),
                any(Pageable.class)
        )).thenReturn(List.of());

        EventResponse<List<EventResponse.EventData>> res = eventServiceImpl.getEvents(filter);
        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertTrue(res.getData().isEmpty());

        verify(eventRepository, times(1))
                .findAllEvents(eq(filter.getSearch()), any(Pageable.class));
        verify(eventRepository, never()).findUpcomingEvents(any(Pageable.class));
        verify(eventRepository, never()).findPopularEvents(any(Pageable.class));
    }
}
