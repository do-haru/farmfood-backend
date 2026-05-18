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
        return searchBlog(query, "date");
    }

    public NaverSearchResponse searchBlog(String query, String sort) {
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/blog.json")
                .queryParam("query", query)
                .queryParam("display", 30)
                .queryParam("start", 1)
                .queryParam("sort", sort)
                .build()
                .encode()
                .toUri();

        return exchange(url);
    }

    public NaverSearchResponse searchNews(String query) {
        return searchNews(query, "date");
    }

    public NaverSearchResponse searchNews(String query, String sort) {
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/news.json")
                .queryParam("query", query)
                .queryParam("display", 30)
                .queryParam("start", 1)
                .queryParam("sort", sort)
                .build()
                .encode()
                .toUri();

        return exchange(url);
    }

    public NaverSearchResponse searchCafe(String query) {
        return searchCafe(query, "date");
    }

    public NaverSearchResponse searchCafe(String query, String sort) {
        URI url = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/cafearticle.json")
                .queryParam("query", query)
                .queryParam("display", 30)
                .queryParam("start", 1)
                .queryParam("sort", sort)
                .build()
                .encode()
                .toUri();

        return exchange(url);
    }

    public NaverSearchResponse searchShopping(String query) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/search/shop.json")
                .queryParam("query", query)
                .queryParam("display", 30)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .build()
                .encode()
                .toUri();

        return exchange(uri);
    }

    private NaverSearchResponse exchange(URI uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        ResponseEntity<NaverSearchResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                NaverSearchResponse.class
        );

        NaverSearchResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("네이버 검색 API 응답이 비어 있습니다: " + uri);
        }
        return body;
    }
}
