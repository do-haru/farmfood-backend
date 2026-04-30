package com.riansoft.farmfood.external.naver;

import com.riansoft.farmfood.external.naver.dto.NaverSearchResponse;
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
public class NaverSearchClient {

    private final RestTemplate restTemplate;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public NaverSearchResponse searchBlog(String query) {
        URI  url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/blog.json")
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "date")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                NaverSearchResponse.class
        );

        return response.getBody();
    }

    public NaverSearchResponse searchNews(String query) {
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/news.json")
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "date")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                NaverSearchResponse.class
        );

        return response.getBody();
    }

    public NaverSearchResponse searchCafe(String query) {
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/cafearticle.json")
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "date")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                NaverSearchResponse.class
        );

        return response.getBody();
    }

    public NaverSearchResponse searchShopping(String query) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/shop.json")
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                NaverSearchResponse.class
        );

        return response.getBody();
    }
}
