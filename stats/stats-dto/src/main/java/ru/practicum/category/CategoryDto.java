package ru.practicum.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class CategoryDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;

}
