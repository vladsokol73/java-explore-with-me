package ru.practicum.explore.hits.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.endpointhit.EndpointHitDto;
import ru.practicum.endpointhit.ViewStats;
import ru.practicum.explore.hits.service.EndpointHitService;

import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
public class EndpointHitController {

    private final EndpointHitService endpointHitService;

    @PostMapping(path = "/hit")
    public ResponseEntity<Void> creatHit(@RequestBody EndpointHitDto endpointHitDto) {
        endpointHitService.creat(endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/stats")
    public List<ViewStats> getStat(@RequestParam (value = "start") String start,
                                   @RequestParam (value = "end") String end,
                                   @RequestParam (value = "uris") List<String> uris,
                                   @RequestParam (value = "unique", defaultValue = "false") Boolean unique) {
        return endpointHitService.getStat(start, end, uris, unique);
    }
}
