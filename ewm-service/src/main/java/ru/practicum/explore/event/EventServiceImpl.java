package ru.practicum.explore.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit.ViewStats;
import ru.practicum.event.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.error.BadRequest;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.location.Location;
import ru.practicum.explore.location.LocationMapper;
import ru.practicum.explore.location.LocationRepository;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.request.RequestStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private final StatsClient statsClient;



    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, LocationRepository locationRepository,
                            RequestRepository requestRepository, StatsClient statsClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
        this.statsClient = statsClient;
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        List<Event> list = eventRepository.findEventsByInitiatorId(userId, PageRequest.of(from, size));
        return list
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto eventDto) {
        if (Objects.isNull(eventDto.getAnnotation()) || Objects.isNull(eventDto.getDescription())) {
            throw new BadRequest("Invalid request");
        }
        if (LocalDateTime.parse(eventDto.getEventDate(), dtf).isBefore(LocalDateTime.now().minusHours(2L))) {
            throw new ConflictException("Field: eventDate. Error: must contain a date that has not yet arrived." +
                    " Value:" + LocalDateTime.now());
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        Event event = EventMapper.toEvent(eventDto);
        event.setCategory(categoryRepository.findById(eventDto.getCategory()).get());
        setConfirmedRequests(event);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(optionalUser.get());
        event.setLocation(addLocation(eventDto.getLocation()));
        event.setState(State.PENDING);
        event.setViews(0L);
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        User optionalUser = userRepository.findById(userId)
                .orElseThrow(() ->  new NotFoundException("User with id=" + userId + "was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->  new NotFoundException("Event with id=" + eventId + "was not found"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getState().equals(State.CANCELED)
                || event.getState().equals(State.PENDING)) {

            if (Objects.nonNull(eventUserRequest.getAnnotation())) {
                event.setAnnotation(eventUserRequest.getAnnotation());
            }
            if (Objects.nonNull(eventUserRequest.getCategory())) {
                event.setCategory(categoryRepository.findById(eventUserRequest.getCategory()).get());
            }
            if (Objects.nonNull(eventUserRequest.getDescription())) {
                event.setDescription(eventUserRequest.getDescription());
            }
            if (Objects.nonNull(eventUserRequest.getEventDate())) {
                event.setEventDate(LocalDateTime.parse(eventUserRequest.getEventDate(), dtf));
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
                    throw new ConflictException("Event cannot be earlier than two hours from the current moment");
                }
            }
            if (Objects.nonNull(eventUserRequest.getLocation())) {
                event.setLocation(LocationMapper.toLocation(eventUserRequest.getLocation()));
            }
            if (Objects.nonNull(eventUserRequest.getPaid())) {
                event.setPaid(eventUserRequest.getPaid());
            }
            if (Objects.nonNull(eventUserRequest.getParticipantLimit())) {
                event.setParticipantLimit(eventUserRequest.getParticipantLimit());
            }
            if (Objects.nonNull(eventUserRequest.getRequestModeration())) {
                event.setRequestModeration(eventUserRequest.getRequestModeration());
            }
            if (Objects.nonNull(eventUserRequest.getTitle())) {
                event.setTitle(eventUserRequest.getTitle());
            }
            switch (eventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
            eventRepository.save(event);
            return EventMapper.toEventFullDto(event);
        } else {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
    }

    @Override
    public List<EventFullDto> searchEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<User> userList = userRepository.findAllById(users);
        List<State> stateList = new ArrayList<>();
        if (Objects.nonNull(states)) {
            for (String s : states) {
                stateList.add(State.valueOf(s));
            }
        }
        List<Category> categoryList = categoryRepository.findAllById(categories);
        LocalDateTime start;
        LocalDateTime end;
        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            start = LocalDateTime.now().withNano(0);
            end = LocalDateTime.now().withNano(0).plusYears(1000L);
        } else {
            start = LocalDateTime.parse(rangeStart, dtf);
            end = LocalDateTime.parse(rangeEnd, dtf);
        }
        List<Event> list =
                eventRepository.findEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(
                        userList, stateList, categoryList, start, end, PageRequest.of(from, size));
        return list
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + "was not found"));
        if (Objects.nonNull(eventAdminRequest.getEventDate())) {
            if (LocalDateTime.parse(eventAdminRequest.getEventDate(), dtf)
                            .isBefore(LocalDateTime.now().plusHours(1L))) {
                throw new ConflictException("Event cannot be earlier than one hours from the current moment");
            }
        }
        if (Objects.nonNull(eventAdminRequest.getAnnotation())) {
            event.setAnnotation(eventAdminRequest.getAnnotation());
        }
        if (Objects.nonNull(eventAdminRequest.getCategory())) {
            event.setCategory(categoryRepository.findById(eventAdminRequest.getCategory()).get());
        }
        if (Objects.nonNull(eventAdminRequest.getDescription())) {
            event.setDescription(eventAdminRequest.getDescription());
        }
        if (Objects.nonNull(eventAdminRequest.getEventDate())) {
            event.setEventDate(LocalDateTime.parse(eventAdminRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (Objects.nonNull(eventAdminRequest.getLocation())) {
            event.setLocation(addLocation(eventAdminRequest.getLocation()));
        }
        if (Objects.nonNull(eventAdminRequest.getPaid())) {
            event.setPaid(eventAdminRequest.getPaid());
        }
        if (Objects.nonNull(eventAdminRequest.getParticipantLimit())) {
            event.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }
        if (Objects.nonNull(eventAdminRequest.getRequestModeration())) {
            event.setRequestModeration(eventAdminRequest.getRequestModeration());
        }
        if (Objects.nonNull(eventAdminRequest.getTitle())) {
            event.setTitle(eventAdminRequest.getTitle());
        }

        if (eventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            if (event.getState().equals(State.PENDING)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                throw new ConflictException("Cannot publish the event because it's not in the right state: PENDING");
            }
        }
        if (Objects.nonNull(eventAdminRequest.getStateAction())) {
            if (eventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                if (event.getState().equals(State.PUBLISHED)) {
                    throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
                } else {
                    event.setState(State.CANCELED);
                }
            }
        }
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsWithFilters(String text, List<Long> categories, Boolean paid,
                                                    String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                    String sort, Integer from, Integer size) {
        List<Category> categoryList;
        if (Objects.nonNull(categories)) {
            categoryList = categoryRepository.findAllById(categories);
        } else {
            categoryList = categoryRepository.findAll();
        }
        LocalDateTime start;
        LocalDateTime end;
        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            start = LocalDateTime.now().withNano(0);
            end = LocalDateTime.now().withNano(0).plusYears(1000L);
        } else {
            start = LocalDateTime.parse(rangeStart, dtf);
            end = LocalDateTime.parse(rangeEnd, dtf);
        }

        List<Event> list = eventRepository
                .findAllByAnnotationContainingOrDescriptionContainingAndCategoryInAndPaidAndEventDateBetween(
                        text, text, categoryList, paid, start, end);
        list.forEach(this::setConfirmedRequests);
        List<Event> sortedList = new ArrayList<>();
        if (Objects.nonNull(sort)) {
            if (sort.equals("EVENT_DATE")) {
                sortedList = list.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            }
            if (sort.equals("VIEWS")) {
                sortedList = list.stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .collect(Collectors.toList());
            }
        }
        String s = LocalDateTime.now().minusDays(10L).format(dtf);
        String s2 = LocalDateTime.now().format(dtf);
        statsClient.getStat(s, s2, "/events", false);
        return sortedList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + "was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + id + "was not found");
        }
        setConfirmedRequests(event);
        String s = LocalDateTime.now().minusDays(10L).format(dtf);
        String s2 = LocalDateTime.now().format(dtf);
        ResponseEntity<List<ViewStats>> responseList = statsClient.getStat(s, s2, "/events/" + id, false);
        List<ViewStats> list = responseList.getBody();
        if (Objects.nonNull(list) && list.size() != 0) {
            event.setViews(list.get(0).getHits());
        }
        return EventMapper.toEventFullDto(event);
    }

    private Location addLocation(LocationDto locationDto) {
        return locationRepository.saveAndFlush(LocationMapper.toLocation(locationDto));
    }

    private void setConfirmedRequests(Event event) {
         event.setConfirmedRequests(requestRepository
                .countParticipationByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED));
    }
}
