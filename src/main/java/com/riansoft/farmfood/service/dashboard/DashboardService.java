package com.riansoft.farmfood.service.dashboard;

import com.riansoft.farmfood.controller.dashboard.dto.DashboardRankingResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardRisingKeywordResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardShoppingTrendResponse;
import com.riansoft.farmfood.domain.metric.MetricType;
import com.riansoft.farmfood.domain.ranking.RankingType;
import com.riansoft.farmfood.repository.metric.KeywordTrendMetricRepository;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<DashboardRankingResponse> getNaverRankings() {
        return trendKeywordRankingRepository.findTop20ByRankingTypeOrderByRankAsc(RankingType.NAVER)
                .stream()
                .map(DashboardRankingResponse::from)
                .toList();
    }

    public List<DashboardRankingResponse> getYoutubeRankings() {
        return trendKeywordRankingRepository.findTop20ByRankingTypeOrderByRankAsc(RankingType.YOUTUBE)
                .stream()
                .map(DashboardRankingResponse::from)
                .toList();
    }

    public List<DashboardRisingKeywordResponse> getNaverRisingKeywords() {
        String recentStart = LocalDate.now().minusDays(7).toString();
        String prevStart = LocalDate.now().minusDays(14).toString();

        return keywordTrendMetricRepository
                .findTop5RisingKeywords(recentStart, prevStart)
                .stream()
                .map(DashboardRisingKeywordResponse::from)
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
