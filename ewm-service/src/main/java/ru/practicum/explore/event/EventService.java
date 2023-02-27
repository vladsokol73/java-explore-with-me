package ru.practicum.explore.event;

import ru.practicum.event.*;

import java.util.List;

public interface EventService {
   List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

   EventFullDto addEvent(Long userId, NewEventDto eventDto);

   EventFullDto getEvent(Long userId, Long eventId);

   EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest);

    List<EventFullDto> searchEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                    String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventAdminRequest);

    List<EventShortDto> getEventsWithFilters(String text, List<Long> categories, Boolean paid, String rangeStart,
                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getEvent(Long id);
}
