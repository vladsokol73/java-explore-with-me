package ru.practicum.explore.hits.service;

import ru.practicum.endpointhit.EndpointHitDto;
import ru.practicum.endpointhit.ViewStats;
import ru.practicum.explore.hits.dto.EndpointHit;

import java.util.List;

public interface EndpointHitService {
    EndpointHit creat(EndpointHitDto endpointHitDto);

    List<ViewStats> getStat(String start, String end, List<String> uris, Boolean unique);
}