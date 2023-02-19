package ru.practicum.explore.hits;

import ru.practicum.EndpointHit.EndpointHitDto;
import ru.practicum.EndpointHit.ViewStats;

import java.util.List;

public interface EndpointHitService {
    EndpointHit creat(EndpointHitDto endpointHitDto);

    List<ViewStats> getStat(String start, String end, List<String> uris, Boolean unique);
}