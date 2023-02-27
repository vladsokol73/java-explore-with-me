package ru.practicum.explore.request;

import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addRequestByUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequestDto> getInfoAboutRequestsByUser(Long userId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);
}
