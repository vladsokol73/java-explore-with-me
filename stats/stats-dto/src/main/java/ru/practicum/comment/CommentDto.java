package ru.practicum.comment;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CommentDto {
    private Long id;

    @NotEmpty
    private String text;

    private String authorName;

    private String created;

    private String state;
}
