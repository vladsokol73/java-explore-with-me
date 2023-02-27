package ru.practicum.explore.compilation;

import lombok.*;
import ru.practicum.explore.event.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "compilation_event",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> events;

    private Boolean pinned;

    private String title;
}
