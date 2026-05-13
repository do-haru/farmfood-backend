package com.riansoft.farmfood.external.youtube;

import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchResponse;
import com.riansoft.farmfood.external.youtube.dto.YoutubeVideoStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class YoutubeSearchClient {

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String apiKey;

    public YoutubeSearchResponse search(String query) {
        return search(query, 50);
    }

    public YoutubeSearchResponse search(String query, int maxResults) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/youtube/v3/search")
                .queryParam("q", query)
                .queryParam("part", "snippet")
                .queryParam("type", "video")
                .queryParam("maxResults", maxResults)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();

        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<YoutubeSearchResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                YoutubeSearchResponse.class
        );

        return response.getBody();
    }

    public YoutubeVideoStatisticsResponse getVideoStatistics(List<String> videoIds) {
        String ids = String.join(",", videoIds);

        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/youtube/v3/videos")
                .queryParam("id", ids)
                .queryParam("part", "statistics")
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();

        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<YoutubeVideoStatisticsResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                YoutubeVideoStatisticsResponse.class
        );

        return response.getBody();
    }
}
