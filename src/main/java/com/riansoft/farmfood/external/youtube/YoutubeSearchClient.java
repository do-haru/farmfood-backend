package com.riansoft.farmfood.external.youtube;

import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchResponse;
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

@Component
@RequiredArgsConstructor
public class YoutubeSearchClient {

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String apiKey;

    public YoutubeSearchResponse search(String query) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/youtube/v3/search")
                .queryParam("q", query)
                .queryParam("part", "snippet")
                .queryParam("type", "video")
                .queryParam("maxResults", 50)
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
}
