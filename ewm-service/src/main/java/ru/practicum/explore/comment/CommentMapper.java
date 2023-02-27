package ru.practicum.explore.comment;

import ru.practicum.comment.CommentDto;

import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated().format(DTF))
                .state(comment.getState())
                .build();
    }
}
