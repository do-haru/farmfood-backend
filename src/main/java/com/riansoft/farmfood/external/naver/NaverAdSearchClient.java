package com.riansoft.farmfood.external.naver;

import com.riansoft.farmfood.external.naver.dto.NaverAdKeywordItem;
import com.riansoft.farmfood.external.naver.dto.NaverAdKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NaverAdSearchClient {

    private final RestTemplate restTemplate;

    @Value("${naver.ad.customer-id}")
    private String customerId;

    @Value("${naver.ad.access-license}")
    private String accessLicense;


    @Value("${naver.ad.secret-key}")
    private String secretKey;

    private static final String BASE_URL = "https://api.searchad.naver.com";
    private static final String KEYWORD_PATH = "/keywordstool";

    public long getMonthlySearchCount(String keyword) {
        URI uri = UriComponentsBuilder
                .fromUriString(BASE_URL + KEYWORD_PATH)
                .queryParam("hintKeywords", keyword)
                .queryParam("showDetail", 1)
                .build()
                .encode()
                .toUri();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Timestamp", timestamp);
        headers.set("X-API-KEY", accessLicense);
        headers.set("X-Customer", customerId);
        headers.set("X-Signature", generateSignature(timestamp, "GET", KEYWORD_PATH));

        ResponseEntity<NaverAdKeywordResponse> response = restTemplate.exchange(
                uri, HttpMethod.GET, new HttpEntity<>(headers), NaverAdKeywordResponse.class
        );

        NaverAdKeywordResponse body = response.getBody();
        if (body == null || body.getKeywordList() == null) return 0;

        List<NaverAdKeywordItem> list = body.getKeywordList();
        return list.stream()
                .filter(item -> keyword.equals(item.getRelKeyword()))
                .findFirst()
                .map(NaverAdKeywordItem::getTotalMonthlySearchCount)
                .orElse(list.isEmpty() ? 0L : list.get(0).getTotalMonthlySearchCount());
    }

    private String generateSignature(String timestamp, String method, String path) {
        try {
            String message = timestamp + "." + method + "." + path;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("SA API 서명 생성 실패", e);
        }
    }
}
