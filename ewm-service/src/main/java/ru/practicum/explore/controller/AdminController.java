package ru.practicum.explore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentDto;
import ru.practicum.compilations.CompilationDto;
import ru.practicum.category.CategoryDto;
import ru.practicum.category.NewCategoryDto;
import ru.practicum.compilations.NewCompilationDto;
import ru.practicum.compilations.UpdateCompilationRequest;
import ru.practicum.event.EventFullDto;
import ru.practicum.event.UpdateEventAdminRequest;
import ru.practicum.explore.category.CategoryService;
import ru.practicum.explore.comment.CommentService;
import ru.practicum.explore.compilation.CompilationService;
import ru.practicum.explore.event.EventService;
import ru.practicum.explore.user.UserService;
import ru.practicum.user.NewUserRequest;
import ru.practicum.user.UserDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("admin/")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    private final CommentService commentService;

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam (name = "ids") ArrayList<Long> ids,
                                  @RequestParam (name = "from", defaultValue = "0") Integer from,
                                  @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.addUser(newUserRequest));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.addCategory(newCategoryDto));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(catId, categoryDto));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventFullDto>> searchEventsByAdmin(@RequestParam List<Long> users,
                                                  @RequestParam (required = false) List<String> states,
                                                  @RequestParam List<Long> categories,
                                                  @RequestParam (required = false) String rangeStart,
                                                  @RequestParam (required = false) String rangeEnd,
                                                  @RequestParam (name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventService.searchEventsByAdmin(users, states,
                categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable Long eventId,
                                                           @RequestBody UpdateEventAdminRequest eventAdminRequest) {
        return ResponseEntity.ok(eventService.updateEventByAdmin(eventId, eventAdminRequest));
    }

    @PostMapping("/compilations")
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationService.addCompilation(newCompilationDto));
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId,
                                                            @RequestBody UpdateCompilationRequest compilationRequest) {
        return ResponseEntity.ok(compilationService.updateCompilation(compId, compilationRequest));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByAdmin(@RequestParam (required = false) String text,
                                                               @RequestParam (name = "from", defaultValue = "0") Integer from,
                                                               @RequestParam (name = "size", defaultValue = "10") Integer size,
                                                               @RequestParam (required = false) String rangeStart,
                                                               @RequestParam (required = false) String rangeEnd) {
        return ResponseEntity.ok(commentService.getCommentsByAdmin(text, from, size, rangeStart, rangeEnd));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentByAdmin(@PathVariable Long commentId,
                                                           @RequestParam("approved") Boolean approved) {
        return ResponseEntity.ok(commentService.updateCommentByAdmin(commentId, approved));
    }
}
