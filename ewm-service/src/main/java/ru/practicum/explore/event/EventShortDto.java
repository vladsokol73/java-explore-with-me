package ru.practicum.explore.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explore.category.CategoryDto;
import ru.practicum.explore.user.UserShortDto;

@Data
@Builder
public class EventShortDto {

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private String eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;

}
