package ru.practicum.explore.request;

import org.springframework.stereotype.Service;
import ru.practicum.event.State;
import ru.practicum.explore.error.ConflictException;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.event.Event;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;
import ru.practicum.request.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ParticipationRequestDto addRequestByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + "was not found"));

        Request request = new Request();

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Initiator of the event cannot add a request to participate in his event");
        }
        User user = userRepository.findById(userId).get();

        if (Objects.nonNull(requestRepository.findByEventIdAndRequesterId(eventId, userId))) {
            throw new ConflictException("Can't add a repeat request");
        }
        request.setRequester(user);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("You can't participate in an unpublished event");
        }
        request.setEvent(event);

        if (request.getEvent().getParticipantLimit()
                <= requestRepository.countRequestByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)) {
            throw new ConflictException("Event has reached the limit of requests for participation");
        }

        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        request.setCreated(LocalDateTime.now().withNano(0));
        requestRepository.saveAndFlush(request);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId) {
        return requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId,
                                                               Long eventId,
                                                               EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + "was not found"));

        if (event.getParticipantLimit()
                <= requestRepository.countRequestByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)) {
            throw new ConflictException("Event has reached the limit of requests for participation");
        }
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();

        List<Request> list = requestRepository.findAllByEventIdAndEventInitiatorIdAndIdIn(eventId, userId, requestIds);

        switch (eventRequestStatusUpdateRequest.getStatus()) {
            case "CONFIRMED":
                    for (Request request : list) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        requestRepository.saveAndFlush(request);
                        result.getConfirmedRequests().add(RequestMapper.toParticipationRequestDto(request));
                    }
                break;

            case "REJECTED":
                for (Request request : list) {
                    request.setStatus(RequestStatus.REJECTED);
                    requestRepository.saveAndFlush(request);
                    result.getRejectedRequests().add(RequestMapper.toParticipationRequestDto(request));
                }


        }
        return result;
    }


    @Override
    public List<ParticipationRequestDto> getInfoAboutRequestsByUser(Long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }
}
