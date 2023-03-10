package ru.practicum.explore.hits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.endpointhit.ViewStats;
import ru.practicum.explore.hits.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository  extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.endpointhit.ViewStats(e.app, e.uri, COUNT(distinct e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp >= :start " +
            "AND e.timestamp <= :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> getStatUrisIsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.endpointhit.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp >= :start " +
            "AND e.timestamp <= :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri")
    List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, List<String> uris);
}
