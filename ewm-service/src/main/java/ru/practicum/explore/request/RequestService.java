package ru.practicum.explore.request;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addRequestByUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequestDto> getInfoAboutRequestsByUser(Long userId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);
}
