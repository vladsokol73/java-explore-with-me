package ru.practicum.ewm.client.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewsStatsRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
public class StatsClient {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String application;
    private final String statsServiceUri;
    private final ObjectMapper json;
    private final HttpClient httpClient;
    private final Duration duration = Duration.ofSeconds(2);


    public StatsClient(@Value("${spring.application.name}") String application,
                       @Value("${services.stats-service.uri:http://localhost:9090}") String statsServiceUri,
                       ObjectMapper json) {
        this.application = application;
        this.statsServiceUri = statsServiceUri;
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(duration)
                .build();
    }

    public void hit(HttpServletRequest userRequest) {
        EndpointHitDto hit = EndpointHitDto.builder()
                .app(application)
                .ip(userRequest.getRemoteAddr())
                .uri(userRequest.getRequestURI())
                .timestamp(LocalDateTime.now().toString())
                .build();

        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
                    .BodyPublishers
                    .ofString(json.writeValueAsString(hit));

            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            HttpResponse<Void> response = httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
            log.debug("Responce from stats-service: {}", response);
        } catch (Exception e) {
            log.warn("Cannot recording hit", e);
        }
    }

    public List<EndpointHitDto> getStats(ViewsStatsRequest request) {
        try {
            String queryString = toQueryString(
                    request.toBuilder().application(application).build()
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/stats" + queryString))
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return json.readValue(response.body(), new TypeReference<List<EndpointHitDto>>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                });
            }
            log.debug("Response from stats-service: {}", response);
        } catch (Exception e) {
            log.warn("Cannot get view stats for request " + request, e);
        }
        return Collections.emptyList();
    }

    private String toQueryString(ViewsStatsRequest request) {
        String start = encode(DTF.format(request.getStart()));
        String end = encode(DTF.format(request.getEnd()));

        return String.format("?start=%s&end=%s&unique=%b%&application=%s",
                start, end, true, application);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
