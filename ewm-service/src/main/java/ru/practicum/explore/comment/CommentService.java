package ru.practicum.explore.comment;

import ru.practicum.comment.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto addComment(Long userId, Long eventId, CommentDto commentDto);


    CommentDto updateComment(Long userId, Long commentId, CommentDto commentDto);

    CommentDto getCommentByUser(Long userId, Long commentId);

    List<CommentDto> getCommentsByUser(Long userId);

    List<CommentDto> getCommentsByEvent(Long id);

    Void deleteComment(Long userId, Long commentId);

    List<CommentDto> getCommentsByAdmin(String text, Integer from, Integer size, String rangeStart, String rangeEnd);

    CommentDto updateCommentByAdmin(Long commentId, Boolean approved);
}
