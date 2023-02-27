package ru.practicum.user;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank
    private String email;

    private Long id;

    @NotBlank
    private String name;
}
