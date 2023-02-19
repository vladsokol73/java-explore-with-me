package ru.practicum.explore.request;

import lombok.*;
import ru.practicum.explore.event.Event;
import ru.practicum.explore.user.User;
import ru.practicum.request.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created; //Дата и время создания заявки

    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;  //Идентификатор события

    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;  //Идентификатор пользователя, отправившего заявку

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;  //Статус заявки
}
