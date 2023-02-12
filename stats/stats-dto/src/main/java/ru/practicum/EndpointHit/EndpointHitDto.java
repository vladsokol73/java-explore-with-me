package ru.practicum.EndpointHit;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class EndpointHitDto {


    private String app;


    private String uri;


    private String ip;

    private String timestamp;
}
