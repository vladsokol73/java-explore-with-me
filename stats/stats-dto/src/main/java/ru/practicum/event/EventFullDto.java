package ru.practicum.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class EventFullDto {     //Дата и время - yyyy-MM-dd HH:mm:ss

//    @Min(20)
//    @Max(2000)
    private String annotation;  //Краткое описание события

    private CategoryDto category;  //id категории к которой относится событие

    private Long confirmedRequests;  //Количество одобренных заявок на участие в данном событии

    private String createdOn;  //Дата и время создания события

    @Min(20)
    @Max(7000)
    private String description;  //Полное описание события

    private String eventDate; //Дата и время события

    private Long id;

    private UserShortDto initiator;

    private LocationDto location; //Широта и долгота места проведения события

    private Boolean paid;   //Платное ли событие

    private Long participantLimit;  //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private String publishedOn; //Дата и время публикации события

    private State state;    //Список состояний жизненного цикла события

    /*Нужна ли пре-модерация заявок на участие.
    Если true, то все заявки будут ожидать подтверждения инициатором события.
    Если false - то будут подтверждаться автоматически.
    */
    private Boolean requestModeration;  //Нужна ли пре-модерация заявок на участие

    @Min(3)
    @Max(120)
    private String title;  //Заголовок события

    private Long views;   //Количество просмотрев события

}
