package ru.practicum.explore.hits;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.endpointHit.EndpointHitDto;
import ru.practicum.endpointHit.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHit creat(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        endpointHit.setTimestamp(LocalDateTime.now());
        return endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStat(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldtStart = LocalDateTime.parse(start,dtf);
        LocalDateTime ldtEnd = LocalDateTime.parse(end, dtf);

        List<ViewStats> result;

        if (unique) {
            result = endpointHitRepository.getStatUrisIsUnique(ldtStart, ldtEnd, uris)
                    .stream()
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .collect(Collectors.toList());
        } else {
            result = endpointHitRepository.getStat(ldtStart, ldtEnd, uris)
                    .stream()
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .collect(Collectors.toList());
        }
        return result;
    }
}
