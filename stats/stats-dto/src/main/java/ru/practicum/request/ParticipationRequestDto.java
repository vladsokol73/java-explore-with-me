package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto { //Получение информации о запросах на участие в событии текущего пользователя

    private String created; //Дата и время создания заявки

    private Long event;  //Идентификатор события

    private Long id;  //Идентификатор заявки

    private Long requester;  //Идентификатор пользователя, отправившего заявку

    private String status;  //Статус заявки


}
