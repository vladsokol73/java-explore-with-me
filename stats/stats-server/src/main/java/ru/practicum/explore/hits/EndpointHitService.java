package ru.practicum.explore.hits;

import ru.practicum.EndpointHitDto;
import ru.practicum.EndpointHitDtoResp;

import java.util.List;

public interface EndpointHitService {
    EndpointHit create(EndpointHitDto endpointHitDto);

    List<EndpointHitDtoResp> getStat(String start, String end, List<String> uris, Boolean unique);
}