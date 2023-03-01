package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentDto;
import ru.practicum.event.EventFullDto;
import ru.practicum.event.EventShortDto;
import ru.practicum.event.NewEventDto;
import ru.practicum.event.UpdateEventUserRequest;
import ru.practicum.explore.comment.CommentService;
import ru.practicum.explore.event.EventService;
import ru.practicum.explore.request.RequestService;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class PrivateController {

    private final EventService eventService;
    private final RequestService requestService;
    private final CommentService commentService;

    public PrivateController(EventService eventService, RequestService requestService, CommentService commentService) {
        this.eventService = eventService;
        this.requestService = requestService;
        this.commentService = commentService;
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getEventsByUser(@PathVariable Long userId,
            @RequestParam (name = "from", defaultValue = "0") Integer from,
            @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventService.getEventsByUser(userId, from, size));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId,
                                                 @RequestBody NewEventDto eventDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.addEvent(userId, eventDto));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventByUser(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          @RequestBody UpdateEventUserRequest eventUserRequest) {
        return ResponseEntity.ok(eventService.updateEventByUser(userId, eventId, eventUserRequest));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUser(@PathVariable Long userId,
                                                                           @PathVariable Long eventId) {

        return ResponseEntity.ok(requestService.getRequestsByUser(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestsStatus(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return ResponseEntity.ok(requestService.updateRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest));
    }


    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getInfoAboutRequestsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getInfoAboutRequestsByUser(userId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> addRequestByUser(@PathVariable Long userId,
                                                                    @RequestParam Long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.addRequestByUser(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequestByUser(@PathVariable Long userId,
                                                                       @PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.cancelRequestByUser(userId, requestId));
    }

    @PostMapping("/{userId}/comments/{eventId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                                 @RequestBody CommentDto commentDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.addComment(userId, eventId, commentDto));
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.updateComment(userId, commentId, commentDto));
    }

    @GetMapping("/{userId}/comments/{commentId}") //Получение информации о комментарии добавленном текущим пользователем
    public ResponseEntity<CommentDto> getComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getCommentByUser(userId, commentId));
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(commentService.deleteComment(userId, commentId));
    }

    @GetMapping("/{userId}/comments") //Получение комментариев добавленных текущим пользователем
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getCommentsByUser(userId));
    }
}
