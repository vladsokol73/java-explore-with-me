package ru.practicum.explore.event;

import lombok.*;
import ru.practicum.event.State;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.location.Location;
import ru.practicum.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "events")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String annotation;  //Краткое описание события

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;  //id категории к которой относится событие

    private Long confirmedRequests;  //Количество одобренных заявок на участие в данном событии

    private LocalDateTime createdOn;  //Дата и время создания события

    @Column(length = 7000)
    private String description;  //Полное описание события

    private LocalDateTime eventDate; //Дата и время события

    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location; //Широта и долгота места проведения события

    private Boolean paid;   //Платное ли событие

    private Long participantLimit;  //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private LocalDateTime  publishedOn; //Дата и время публикации события

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;  //Список состояний жизненного цикла события


    /*Нужна ли пре-модерация заявок на участие.
    Если true, то все заявки будут ожидать подтверждения инициатором события.
    Если false - то будут подтверждаться автоматически.
    */
    private Boolean requestModeration;

    private String title;  //Заголовок события

    private Long views;
}
