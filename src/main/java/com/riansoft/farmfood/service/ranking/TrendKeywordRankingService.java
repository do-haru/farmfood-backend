package com.riansoft.farmfood.service.ranking;

import com.riansoft.farmfood.domain.ranking.PeriodType;
import com.riansoft.farmfood.domain.ranking.RankingType;
import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.keyword.KeywordFrequencySummary;
import com.riansoft.farmfood.repository.metric.KeywordDailySearchEstimateRepository;
import com.riansoft.farmfood.repository.metric.YoutubeKeywordMetricRepository;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendKeywordRankingService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final TrendKeywordRankingRepository trendKeywordRankingRepository;
    private final KeywordDailySearchEstimateRepository keywordDailySearchEstimateRepository;
    private final YoutubeKeywordMetricRepository youtubeKeywordMetricRepository;

    @Transactional
    public void calculateNaverRanking() {
        LocalDate latestDate = keywordDailySearchEstimateRepository
                .findLatestSearchDate()
                .orElse(LocalDate.now().minusDays(1));

        for (PeriodType periodType : PeriodType.values()) {
            calculateNaverRankingForPeriod(periodType, latestDate);
        }
    }

    private void calculateNaverRankingForPeriod(PeriodType periodType, LocalDate latestDate) {

        LocalDate from = switch (periodType) {
            case DAILY -> latestDate;
            case WEEKLY -> latestDate.minusDays(6);
            case MONTHLY -> latestDate.minusDays(29);
            case YEARLY -> latestDate.minusDays(364);
        };

        List<Object[]> results = keywordDailySearchEstimateRepository
                .findKeywordSearchCountSumBetween(from, latestDate);

        if (results.isEmpty()) return;

        LocalDateTime rankedAt = LocalDateTime.now();
        int rank = 1;
        for (Object[] row : results) {
            String keyword = (String) row[0];
            long searchCount = ((Number) row[1]).longValue();
            trendKeywordRankingRepository.save(new TrendKeywordRanking(
                    RankingType.NAVER, periodType, keyword, searchCount, rank++, rankedAt
            ));
        }
    }

    @Transactional
    public void calculateYoutubeRanking() {
        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummariesBySourceTypes(
                        List.of(SourceType.YOUTUBE), PageRequest.of(0, 30)
                );

        if (keywords.isEmpty()) return;

        record KeywordScore(String keyword, long engagementScore) {}

        List<KeywordScore> scored = keywords.stream()
                .map(k -> {
                    Long score = youtubeKeywordMetricRepository.calculateEngagementScore(k.getKeyword());
                    return new KeywordScore(k.getKeyword(), score != null ? score : 0L);
                })
                .sorted(Comparator.comparingLong(KeywordScore::engagementScore).reversed())
                .toList();

        LocalDateTime rankedAt = LocalDateTime.now();
        for (int i = 0; i < scored.size(); i++) {
            KeywordScore s = scored.get(i);
            trendKeywordRankingRepository.save(new TrendKeywordRanking(
                    RankingType.YOUTUBE,
                    PeriodType.MONTHLY,
                    s.keyword(),
                    s.engagementScore(),
                    i + 1,
                    rankedAt
            ));
        }
    }

    @Transactional
    public void calculateRanking() {
        calculateNaverRanking();
        calculateYoutubeRanking();
    }
}
