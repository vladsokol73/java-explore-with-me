package ru.practicum.compilations;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {

    private List<EventShortDto> events;

    private Long id;

    private boolean pinned;

    private String title;
}
