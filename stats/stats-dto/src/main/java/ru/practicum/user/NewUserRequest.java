package ru.practicum.user;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    private String email;

    private String name;
}
