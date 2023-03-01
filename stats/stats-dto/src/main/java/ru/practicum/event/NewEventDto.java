package ru.practicum.event;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class NewEventDto {

    @Min(20)
    @Max(2000)
    private String annotation;  //Краткое описание события

    private Long category;  //id категории к которой относится событие

    @Min(20)
    @Max(7000)
    private String description;  //Полное описание события

    private String eventDate; //Дата и время события - yyyy-MM-dd HH:mm:ss

    private LocationDto location; //Широта и долгота места проведения события

    private Boolean paid;   //Платное ли событие

    private Long participantLimit;  //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    /*Нужна ли пре-модерация заявок на участие.
    Если true, то все заявки будут ожидать подтверждения инициатором события.
    Если false - то будут подтверждаться автоматически.
    */
    private Boolean requestModeration;

    private String title;  //Заголовок события
}
