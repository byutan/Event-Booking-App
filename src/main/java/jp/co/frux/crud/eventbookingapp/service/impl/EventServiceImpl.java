package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.EventFilterRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;
import jp.co.frux.crud.eventbookingapp.entity.Event;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.EventRepository;
import jp.co.frux.crud.eventbookingapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public EventResponse<EventResponse.EventData> createEvent(EventRequest req) {
        String title = req.getTitle();
        LocalDateTime dateTime = req.getDateTime();
        String location = req.getLocation();
        Double latitude = req.getLatitude();
        Double longitude = req.getLongitude();
        Double price = req.getPrice();
        String description = req.getDescription();
        String imageUrl = req.getImageUrl();
        LocalDateTime createdAt = LocalDateTime.now();

        Event newEvent = new Event(
                title,
                dateTime,
                location,
                latitude,
                longitude,
                price,
                description,
                imageUrl,
                createdAt);

        Event savedEvent = eventRepository.save(newEvent);
        boolean isSuccess = true;
        String message = "Event created successfully";

        return EventResponse.<EventResponse.EventData>builder()
                .success(isSuccess)
                .message(message)
                .data(new EventResponse.EventData(
                        savedEvent.getId(),
                        savedEvent.getTitle(),
                        savedEvent.getDateTime(),
                        savedEvent.getLocation(),
                        savedEvent.getPrice(),
                        savedEvent.getDescription(),
                        savedEvent.getImageUrl()
                ))
                .build();
    }

    @Override
    public EventResponse<EventResponse.EventData> updateEvent(Integer id, EventRequest req) {
        String message = "Event not found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        Event event =  eventRepository.findById(id).orElseThrow(() -> new BusinessValidationException(message, status));

        LocalDateTime updatedAt = LocalDateTime.now();

        event.setTitle(req.getTitle());
        event.setDateTime(req.getDateTime());
        event.setLocation(req.getLocation());
        event.setLatitude(req.getLatitude());
        event.setLongitude(req.getLongitude());
        event.setPrice(req.getPrice());
        event.setDescription(req.getDescription());
        event.setImageUrl(req.getImageUrl());
        event.setUpdatedAt(updatedAt);

        Event savedEvent = eventRepository.save(event);

        boolean isSuccess = true;
        String returnMessage = "Event updated successfully";

        return EventResponse.<EventResponse.EventData>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(new EventResponse.EventData(
                        savedEvent.getId(),
                        savedEvent.getTitle(),
                        savedEvent.getDateTime(),
                        savedEvent.getLocation(),
                        savedEvent.getPrice(),
                        savedEvent.getDescription(),
                        savedEvent.getImageUrl()
                ))
                .build();
    }

    @Override
    public EventResponse<List<EventResponse.EventData>> getEvents(EventFilterRequest filter) {
        int pageIndex = filter.getPage() - 1;
        Pageable pageable = PageRequest.of(pageIndex, filter.getSize());
        String type = filter.getType();
        String search = filter.getSearch();

        List<Event> events = new ArrayList<>();
        if(type == null) {
            events = eventRepository.findAllEvents(search, pageable);
        } else {
            events = switch (type) {
                case "nearby" -> eventRepository.findNearbyEvents(filter.getLatitude(), filter.getLongitude(), pageable);
                case "upcoming" -> eventRepository.findUpcomingEvents(pageable);
                case "popular" -> eventRepository.findPopularEvents(pageable);
                default -> events;
            };
            if (search != null) {
                System.out.println(search);
                events = events.stream().filter(e -> e.getTitle().contains(search)).collect(Collectors.toList());
            }
        }

        boolean isSuccess = true;
        String message = "Events fetched successfully";

        return EventResponse.<List<EventResponse.EventData>>builder()
                .success(isSuccess)
                .message(message)
                .data(events.stream()
                        .map(this::mapToEventData)
                        .toList())
                .build();
    }

    @Override
    public EventResponse<EventResponse.EventData> getEventById(Integer id) {
        String message = "Event not found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        Event event =  eventRepository.findById(id).orElseThrow(() -> new BusinessValidationException(message, status));
        boolean isSuccess = true;
        String returnMessage = "Event detail fetched successfully";

        return EventResponse.<EventResponse.EventData>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(new EventResponse.EventData(
                        event.getId(),
                        event.getTitle(),
                        event.getDateTime(),
                        event.getLocation(),
                        event.getPrice(),
                        event.getDescription(),
                        event.getImageUrl()
                ))
                .build();
    }

    @Override
    public void deleteEvent(Integer id) {
        String message = "Event not found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        Event event =  eventRepository.findById(id).orElseThrow(() -> new BusinessValidationException(message, status));
        eventRepository.delete(event);
    }

    private EventResponse.EventData mapToEventData(Event event) {
        return new EventResponse.EventData(
                event.getId(),
                event.getTitle(),
                event.getDateTime(),
                event.getLocation(),
                event.getPrice(),
                event.getDescription(),
                event.getImageUrl()
        );
    }
}
