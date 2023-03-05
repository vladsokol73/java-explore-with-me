package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comment.CommentDto;
import ru.practicum.explore.compilation.CompilationDto;
import ru.practicum.explore.category.CategoryDto;
import ru.practicum.explore.event.EventFullDto;
import ru.practicum.explore.event.EventShortDto;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.explore.category.CategoryService;
import ru.practicum.explore.comment.CommentService;
import ru.practicum.explore.compilation.CompilationService;
import ru.practicum.explore.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final StatsClient serverClient;
    private final CommentService commentService;

    public PublicController(CategoryService categoryService, EventService eventService,
                            CompilationService compilationService, StatsClient serverClient, CommentService commentService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
        this.serverClient = serverClient;
        this.commentService = commentService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam (name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(categoryService.getCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId) {
        return ResponseEntity.ok(categoryService.getCategory(catId));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> getEventsWithFilters(@RequestParam (required = false) String text,
                                                        @RequestParam (required = false) List<Long> categories,
                                                        @RequestParam (required = false) Boolean paid,
                                                        @RequestParam (required = false) String rangeStart,
                                                        @RequestParam (required = false) String rangeEnd,
                                                        @RequestParam (required = false) Boolean onlyAvailable,
                                                        @RequestParam (required = false) String sort,
                                                        @RequestParam (name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam (name = "size", defaultValue = "10") Integer size,
                                                        HttpServletRequest httpServletRequest) {
        serverClient.creatHit(httpServletRequest);
        return ResponseEntity.ok(eventService.getEventsWithFilters(text, categories, paid, rangeStart,
                        rangeEnd, onlyAvailable, sort, from, size));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id,
                                                 HttpServletRequest httpServletRequest) {
        serverClient.creatHit(httpServletRequest);
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam (defaultValue = "false") Boolean pinned,
                                                    @RequestParam (name = "from", defaultValue = "0") Integer from,
                                                    @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {
        return ResponseEntity.ok(compilationService.getCompilation(compId));
    }

    @GetMapping("/events/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByEvent(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByEvent(id));
    }
}
