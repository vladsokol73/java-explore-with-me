package ru.practicum.explore.event;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.State;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findEventsByCategory(Category category);

    List<Event> findEventsByInitiator(User user, Pageable pageable);

    List<Event> findAllByAnnotationContainingOrDescriptionContainingAndCategoryInAndPaidAndEventDateBetween(
            String s, String s2, List<Category> categoryList, Boolean paid, LocalDateTime start, LocalDateTime end);


    List<Event> findEventsByInitiatorInAndStateInAndCategoryInAndEventDateBetween(
            List<User> userList, List<State> stateList,
            List<Category> categoryList, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> findAllByIdIn(List<Long> list);

}
