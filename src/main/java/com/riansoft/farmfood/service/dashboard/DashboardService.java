package com.riansoft.farmfood.service.dashboard;

import com.riansoft.farmfood.controller.dashboard.dto.DashboardRankingResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardShoppingTrendResponse;
import com.riansoft.farmfood.domain.metric.MetricType;
import com.riansoft.farmfood.repository.metric.KeywordTrendMetricRepository;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TrendKeywordRankingRepository trendKeywordRankingRepository;
    private final KeywordTrendMetricRepository keywordTrendMetricRepository;

    public List<DashboardRankingResponse> getTrendKeywordRankings() {
        return trendKeywordRankingRepository.findTop20ByOrderByRankAsc()
                .stream()
                .map(DashboardRankingResponse::from)
                .toList();
    }

    public List<DashboardShoppingTrendResponse> getShoppingTrends(String keyword) {
        return keywordTrendMetricRepository
                .findByKeywordAndMetricTypeOrderByPeriodAsc(
                        keyword,
                        MetricType.SHOPPING_TREND
                )
                .stream()
                .map(DashboardShoppingTrendResponse::from)
                .toList();
    }
}
