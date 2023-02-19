package ru.practicum.explore.error;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private List<String> errors; //Список стектрейсов или описания ошибок

    private String message;  //Сообщение об ошибке

    private String reason;  //Общее описание причины ошибки

    private HttpStatus status;

    private LocalDateTime timestamp;

}
