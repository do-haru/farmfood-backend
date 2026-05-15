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
import java.util.ArrayList;
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
        for (PeriodType periodType : PeriodType.values()) {
            calculateNaverRankingForPeriod(periodType);
        }
    }

    private void calculateNaverRankingForPeriod(PeriodType periodType) {
        LocalDate latestDate = keywordDailySearchEstimateRepository
                .findLatestSearchDate()
                .orElse(LocalDate.now().minusDays(1));

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

        int maxFrequency = keywords.get(0).getTotalFrequency();

        List<Long> engagementScores = keywords.stream()
                .map(k -> youtubeKeywordMetricRepository.calculateEngagementScore(k.getKeyword()))
                .toList();

        long maxEngagement = engagementScores.stream().mapToLong(Long::longValue).max().orElse(1L);

        record KeywordScore(String keyword, double frequencyScore, double engagementScore, double finalScore) {}

        List<KeywordScore> scored = new ArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            double freqScore = ((double) keywords.get(i).getTotalFrequency() / maxFrequency) * 100;
            double engScore = maxEngagement > 0 ? ((double) engagementScores.get(i) / maxEngagement) * 100 : 0.0;
            double finalScore = freqScore * 0.5 + engScore * 0.5;
            scored.add(new KeywordScore(keywords.get(i).getKeyword(), freqScore, engScore, finalScore));
        }

        scored.sort(Comparator.comparingDouble(KeywordScore::finalScore).reversed());

        LocalDateTime rankedAt = LocalDateTime.now();
        for (int i = 0; i < scored.size(); i++) {
            KeywordScore s = scored.get(i);
            trendKeywordRankingRepository.save(new TrendKeywordRanking(
                    RankingType.YOUTUBE,
                    PeriodType.MONTHLY,
                    s.keyword(),
                    (long) s.finalScore(),
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
