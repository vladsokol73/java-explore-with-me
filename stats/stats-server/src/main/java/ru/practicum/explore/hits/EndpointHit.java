package ru.practicum.explore.hits;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "hits")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit { //формат "yyyy-MM-dd HH:mm:ss"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column
    private String app; //Идентификатор сервиса для которого записывается информация example: ewm-main-service

    @Column
    private String uri;  //URI для которого был осуществлен запрос example: /events/1

    @Column
    private String ip;  //IP-адрес пользователя, осуществившего запрос example: 192.163.0.1

    @Column
    private LocalDateTime timestamp;  //Дата и время, когда был совершен запрос к эндпоинту
}
