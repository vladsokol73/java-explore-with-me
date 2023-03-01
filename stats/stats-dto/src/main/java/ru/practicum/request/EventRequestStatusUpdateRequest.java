package ru.practicum.request;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;  //Идентификаторы запросов на участие в событии текущего пользователя

    private String status;
}
