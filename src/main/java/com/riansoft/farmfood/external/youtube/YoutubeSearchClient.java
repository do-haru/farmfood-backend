package com.riansoft.farmfood.external.youtube;

import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchResponse;
import com.riansoft.farmfood.external.youtube.dto.YoutubeVideoStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
        return search(query, 30);
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

        ResponseEntity<YoutubeSearchResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                YoutubeSearchResponse.class
        );

        YoutubeSearchResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("유튜브 검색 API 응답이 비어 있습니다: " + uri);
        }
        return body;
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

        ResponseEntity<YoutubeVideoStatisticsResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                YoutubeVideoStatisticsResponse.class
        );

        YoutubeVideoStatisticsResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("유튜브 영상 통계 API 응답이 비어 있습니다: " + uri);
        }
        return body;
    }
}
