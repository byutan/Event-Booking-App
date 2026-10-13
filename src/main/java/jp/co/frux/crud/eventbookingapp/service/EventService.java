package jp.co.frux.crud.eventbookingapp.service;

import jp.co.frux.crud.eventbookingapp.dto.EventFilterRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventRequest;
import jp.co.frux.crud.eventbookingapp.dto.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse<EventResponse.EventData> createEvent(EventRequest req);
    EventResponse<EventResponse.EventData> updateEvent(Integer id, EventRequest req);
    EventResponse<List<EventResponse.EventData>> getEvents(EventFilterRequest filter);
    EventResponse<EventResponse.EventData> getEventById(Integer id);
    void deleteEvent(Integer id);
}
