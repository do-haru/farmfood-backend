package com.riansoft.farmfood.controller.admin;

import com.riansoft.farmfood.service.metric.KeywordSearchCountService;
import com.riansoft.farmfood.service.metric.KeywordTrendMetricService;
import com.riansoft.farmfood.service.metric.YoutubeKeywordMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/metrics")
@RequiredArgsConstructor
public class AdminMetricController {

    private final KeywordTrendMetricService keywordTrendMetricService;
    private final YoutubeKeywordMetricService youtubeKeywordMetricService;
    private final KeywordSearchCountService keywordSearchCountService;

    @PostMapping("/shopping-trends/collect")
    public String collectShoppingTrendMetrics() {
        keywordTrendMetricService.collectShoppingTrendMetrics();

        return "쇼핑 트렌드 지표 수집 완료";
    }

    @PostMapping("/youtube/collect")
    public String collectYoutubeMetrics() {
        youtubeKeywordMetricService.collectMetrics();

        return "유튜브 지표 수집 완료";
    }

    @PostMapping("/search-counts/collect")
    public String collectMonthlySearchCounts() {
        keywordSearchCountService.collectMonthlySearchCounts();

        return "월간 검색수 수집 완료";
    }
}
