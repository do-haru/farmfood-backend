package com.riansoft.farmfood.controller.admin;

import com.riansoft.farmfood.service.metric.KeywordTrendMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/metrics")
@RequiredArgsConstructor
public class AdminMetricController {

    private final KeywordTrendMetricService keywordTrendMetricService;

    @PostMapping("/shopping-trends/collect")
    public String collectShoppingTrendMetrics() {
        keywordTrendMetricService.collectShoppingTrendMetrics();

        return "쇼핑 트렌드 지표 수집 완료";
    }
}
