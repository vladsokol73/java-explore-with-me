package ru.practicum.explore.hits;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.EndpointHitDtoResp;

import java.util.List;

@RestController
@Slf4j
public class EndpointHitController {

    private final EndpointHitService endpointHitService;

    public EndpointHitController(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    @PostMapping(path = "/hit")
    public void creatHit(@RequestBody EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitService.creat(endpointHitDto);
        System.out.println(endpointHit.getIp() + endpointHit.getApp() + endpointHit.getUri());
    }

    @GetMapping(path = "/stats")
    public List<EndpointHitDtoResp> getStat(@RequestParam (value = "start") String start,
                                            @RequestParam (value = "end") String end,
                                            @RequestParam (value = "uris") List<String> uris,
                                            @RequestParam (value = "unique", defaultValue = "false") Boolean unique) {
        return endpointHitService.getStat(start, end, uris, unique);
    }
}
