package ru.practicum.explore.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explore.event.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {

    private List<EventShortDto> events;

    private Long id;

    private boolean pinned;

    private String title;
}
