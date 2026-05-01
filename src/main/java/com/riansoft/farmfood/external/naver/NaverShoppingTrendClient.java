package com.riansoft.farmfood.external.naver;

import com.riansoft.farmfood.external.naver.dto.NaverShoppingTrendKeyword;
import com.riansoft.farmfood.external.naver.dto.NaverShoppingTrendRequest;
import com.riansoft.farmfood.external.naver.dto.NaverShoppingTrendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NaverShoppingTrendClient {

    private final RestTemplate restTemplate;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public NaverShoppingTrendResponse getShoppingTrend(String keyword) {
        String url = "https://openapi.naver.com/v1/datalab/shopping/category/keywords";

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);

        NaverShoppingTrendRequest body = new NaverShoppingTrendRequest(
                startDate.toString(),
                endDate.toString(),
                "date",
                "50000006",
                List.of(new NaverShoppingTrendKeyword(keyword, List.of(keyword)))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<NaverShoppingTrendRequest> request = new HttpEntity<>(body, headers);

        ResponseEntity<NaverShoppingTrendResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                NaverShoppingTrendResponse.class
        );

        return response.getBody();
    }

}
