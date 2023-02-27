package ru.practicum.event;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class UpdateEventUserRequest {  //Дата и время - yyyy-MM-dd HH:mm:ss

    @Min(20)
    @Max(2000)
    private String annotation;  //Новая аннотация

    private Long category;  //Новая категория

    @Min(20)
    @Max(7000)
    private String description;  //Новое описание

    private String eventDate; //Новые дата и время

    private LocationDto location; //Новая Широта и долгота места проведения события

    private Boolean paid;   //Новое значение флага о платности мероприятия

    private Long participantLimit;  //Новый лимит пользователей

    /*Нужна ли пре-модерация заявок на участие.
    Если true, то все заявки будут ожидать подтверждения инициатором события.
    Если false - то будут подтверждаться автоматически.
    */
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    private StateAction stateAction;  //Изменение состояния события

    @Min(3)
    @Max(120)
    private String title;  //Новый заголовок
}
