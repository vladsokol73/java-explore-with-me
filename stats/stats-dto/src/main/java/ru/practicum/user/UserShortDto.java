package ru.practicum.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserShortDto {
    Long id;
    String name;
}
