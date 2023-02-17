package ru.practicum.explore.hits;

import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.EndpointHitDtoResp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndpointHitServiceImpl implements EndpointHitService {

    EndpointHitRepository endpointHitRepository;

    public EndpointHitServiceImpl(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Override
    public EndpointHit create(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        endpointHit.setTimestamp(LocalDateTime.now());
        return endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<EndpointHitDtoResp> getStat(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<EndpointHitDtoResp> result = null;
        try {
            LocalDateTime ldtStart = LocalDateTime.parse(start, dtf);
            LocalDateTime ldtEnd = LocalDateTime.parse(end, dtf);

            if (unique) {
                result = endpointHitRepository.getStatUrisIsUnique(ldtStart, ldtEnd, uris)
                        .stream()
                        .sorted(Comparator.comparing(EndpointHitDtoResp::getHits).reversed())
                        .collect(Collectors.toList());
            } else {
                result = endpointHitRepository.getStat(ldtStart, ldtEnd, uris)
                        .stream()
                        .sorted(Comparator.comparing(EndpointHitDtoResp::getHits).reversed())
                        .collect(Collectors.toList());
            }

        } catch (Exception e) {
        }
        return result;
    }
}
