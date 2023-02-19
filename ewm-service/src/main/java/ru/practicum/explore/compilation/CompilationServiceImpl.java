package ru.practicum.explore.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.CompilationDto;
import ru.practicum.compilations.NewCompilationDto;
import ru.practicum.compilations.UpdateCompilationRequest;
import ru.practicum.explore.error.BadRequest;
import ru.practicum.explore.error.NotFoundException;
import ru.practicum.explore.event.EventRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        if (Objects.isNull(newCompilationDto.getTitle())) {
            throw new BadRequest("Invalid request");
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + "was not found"));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationRequest) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + "was not found"));

        compilation.setEvents(eventRepository.findAllByIdIn(compilationRequest.getEvents()));
        if (Objects.nonNull(compilationRequest.getPinned())) {
            compilation.setPinned(compilationRequest.getPinned());
        }
        if (Objects.nonNull(compilationRequest.getTitle())) {
            compilation.setTitle(compilationRequest.getTitle());
        }
        Compilation compilationResult = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilationResult);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + "was not found"));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
