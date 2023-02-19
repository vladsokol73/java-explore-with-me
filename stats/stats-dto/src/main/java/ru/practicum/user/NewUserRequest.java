package ru.practicum.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @Email(message = "Invalid email")
    @NotBlank
    private String email;

    @NotBlank(message = "Incorrect name")
    private String name;
}
