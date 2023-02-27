package ru.practicum.explore.hits;

import ru.practicum.EndpointHit.EndpointHitDto;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .build();
    }
}
