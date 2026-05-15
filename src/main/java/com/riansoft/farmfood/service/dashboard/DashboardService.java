package com.riansoft.farmfood.service.dashboard;

import com.riansoft.farmfood.controller.dashboard.dto.DashboardRankingResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardRisingKeywordResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardSearchContentResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardShoppingTrendResponse;
import com.riansoft.farmfood.domain.metric.KeywordTrendMetric;
import com.riansoft.farmfood.domain.metric.MetricType;
import com.riansoft.farmfood.domain.ranking.PeriodType;
import com.riansoft.farmfood.domain.ranking.RankingType;
import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import com.riansoft.farmfood.external.naver.NaverSearchClient;
import com.riansoft.farmfood.repository.metric.KeywordSearchCountRepository;
import com.riansoft.farmfood.repository.metric.KeywordTrendMetricRepository;
import com.riansoft.farmfood.repository.metric.YoutubeKeywordMetricRepository;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TrendKeywordRankingRepository trendKeywordRankingRepository;
    private final KeywordTrendMetricRepository keywordTrendMetricRepository;
    private final YoutubeKeywordMetricRepository youtubeKeywordMetricRepository;
    private final KeywordSearchCountRepository keywordSearchCountRepository;
    private final NaverSearchClient naverSearchClient;

    public List<DashboardRankingResponse> getTrendKeywordRankings() {
        return getNaverRankings(PeriodType.DAILY);
    }

    public List<DashboardRankingResponse> getNaverRankings(PeriodType periodType) {
        return getRankingsWithChange(RankingType.NAVER, periodType);
    }

    public List<DashboardRankingResponse> getYoutubeRankings() {
        return getRankingsWithChange(RankingType.YOUTUBE, PeriodType.MONTHLY).stream()
                .limit(20)
                .toList();
    }

    private List<DashboardRankingResponse> getRankingsWithChange(RankingType rankingType, PeriodType periodType) {
        List<TrendKeywordRanking> current = trendKeywordRankingRepository
                .findLatestByRankingTypeAndPeriodType(rankingType, periodType);
        List<TrendKeywordRanking> previous = trendKeywordRankingRepository
                .findPreviousByRankingTypeAndPeriodType(rankingType, periodType);

        Map<String, Integer> previousRankMap = previous.stream()
                .collect(Collectors.toMap(TrendKeywordRanking::getKeyword, TrendKeywordRanking::getRank));

        return current.stream()
                .map(r -> {
                    Integer prevRank = previousRankMap.get(r.getKeyword());
                    Integer rankChange = prevRank != null ? prevRank - r.getRank() : null;
                    return DashboardRankingResponse.from(r, rankChange);
                })
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

    public List<DashboardRisingKeywordResponse> getYoutubeRisingKeywords() {
        return youtubeKeywordMetricRepository
                .findTop5YoutubeRisingKeywords()
                .stream()
                .map(DashboardRisingKeywordResponse::from)
                .toList();
    }

    public List<DashboardSearchContentResponse> getNaverBlogContents(String keyword) {
        return naverSearchClient.searchBlog(keyword, "sim")
                .getItems()
                .stream()
                .limit(4)
                .map(DashboardSearchContentResponse::from)
                .toList();
    }

    public List<DashboardSearchContentResponse> getNaverNewsContents(String keyword) {
        return naverSearchClient.searchNews(keyword, "sim")
                .getItems()
                .stream()
                .limit(4)
                .map(DashboardSearchContentResponse::fromNews)
                .toList();
    }

    public List<DashboardSearchContentResponse> getNaverCafeContents(String keyword) {
        return naverSearchClient.searchCafe(keyword, "sim")
                .getItems()
                .stream()
                .limit(4)
                .map(DashboardSearchContentResponse::from)
                .toList();
    }

    public List<DashboardSearchContentResponse> getNaverShoppingContents(String keyword) {
        return naverSearchClient.searchShopping(keyword)
                .getItems()
                .stream()
                .limit(4)
                .map(DashboardSearchContentResponse::fromShopping)
                .toList();
    }

    public List<DashboardShoppingTrendResponse> getShoppingTrends(String keyword) {
        List<KeywordTrendMetric> metrics = keywordTrendMetricRepository
                .findByKeywordAndMetricTypeOrderByPeriodAsc(keyword, MetricType.SHOPPING_TREND);

        if (metrics.isEmpty()) return List.of();

        String currentYearMonth = YearMonth.now().toString();

        return keywordSearchCountRepository.findByKeywordAndDate(keyword, currentYearMonth)
                .map(searchCount -> {
                    // 이번 달 DataLab 지수 합계 계산
                    double monthlyIndexSum = metrics.stream()
                            .filter(m -> m.getPeriod().startsWith(currentYearMonth))
                            .mapToDouble(KeywordTrendMetric::getValue)
                            .sum();

                    if (monthlyIndexSum <= 0) {
                        return metrics.stream().map(DashboardShoppingTrendResponse::from).toList();
                    }

                    double scaleFactor = searchCount.getSearchCount() / monthlyIndexSum;
                    return metrics.stream()
                            .map(m -> DashboardShoppingTrendResponse.fromScaled(m, scaleFactor))
                            .toList();
                })
                .orElseGet(() -> metrics.stream().map(DashboardShoppingTrendResponse::from).toList());
    }
}
