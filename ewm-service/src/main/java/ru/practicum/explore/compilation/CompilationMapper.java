package ru.practicum.explore.compilation;


import ru.practicum.compilations.CompilationDto;
import ru.practicum.compilations.NewCompilationDto;
import ru.practicum.explore.event.EventMapper;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents()
                        .stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
