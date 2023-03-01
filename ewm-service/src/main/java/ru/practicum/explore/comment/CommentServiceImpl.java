package ru.practicum.explore.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comment.CommentDto;
import ru.practicum.event.State;
import ru.practicum.explore.error.BadRequest;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.event.Event;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CommentDto addComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new NotFoundException("User with id=" + userId + "was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->  new NotFoundException("Event with id=" + eventId + "was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new BadRequest("Comments can only be left for events with the status PUBLISHED");
        }
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setState("PENDING");
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId);
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getCommentByUser(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId);
        if (Objects.isNull(comment)) {
            throw new NotFoundException("Comment");
        }
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByUser(Long userId) {
        List<Comment> list = commentRepository.findAllByAuthorId(userId);
        return list.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Long id) {
        List<Comment> list = commentRepository.findAllByEventIdAndStateIs(id, "PUBLISHED");
        return list.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + "was not found"));
        if (comment.getAuthor().getId().equals(userId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new BadRequest("Only the author of a comment can delete it");
        }
        return null;
    }

    @Override
    public List<CommentDto> getCommentsByAdmin(String text, Integer from, Integer size, String rangeStart, String rangeEnd) {
        LocalDateTime start;
        LocalDateTime end;
        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            start = LocalDateTime.now().withNano(0).minusDays(1L);
            end = LocalDateTime.now().withNano(0).plusMinutes(1L);
        } else {
            start = LocalDateTime.parse(rangeStart, dtf);
            end = LocalDateTime.parse(rangeEnd, dtf);
        }
        List<Comment> list = commentRepository.findAllByTextContainingAndCreatedBetween(text, start, end,
                PageRequest.of(from, size));
        return list.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto updateCommentByAdmin(Long commentId, Boolean approved) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + "was not found"));
        if (approved) {
            comment.setState("PUBLISHED");
        } else comment.setState("CANCELED");
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }
}
