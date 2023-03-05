package ru.practicum.explore.hits.mapper;

import ru.practicum.endpointhit.EndpointHitDto;
import ru.practicum.explore.hits.dto.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .build();
    }
}
