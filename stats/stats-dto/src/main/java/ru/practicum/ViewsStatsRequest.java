package ru.practicum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
public class ViewsStatsRequest {
    String application;
    LocalDateTime start;
    LocalDateTime end;
    String uris;
    String limit;
}
