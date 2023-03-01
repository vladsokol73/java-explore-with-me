package ru.practicum;

import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDtoResp {
    private String app;
    private String uri;
    private Long hits;
}
