package ru.practicum.EndpointHit;

import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
