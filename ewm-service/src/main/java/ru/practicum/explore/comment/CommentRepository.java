package ru.practicum.explore.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTextContainingAndCreatedBetween(String text, LocalDateTime start, LocalDateTime end,
                                                           Pageable pageable);

    List<Comment> findAllByEventIdAndStateIs(Long id, String state);

    Comment findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByAuthorId(Long id);
}
