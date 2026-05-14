package com.riansoft.farmfood.service.ranking;

import com.riansoft.farmfood.domain.ranking.RankingType;
import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.keyword.KeywordFrequencySummary;
import com.riansoft.farmfood.repository.metric.YoutubeKeywordMetricRepository;
import org.springframework.data.domain.PageRequest;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendKeywordRankingService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final TrendKeywordRankingRepository trendKeywordRankingRepository;
    private final YoutubeKeywordMetricRepository youtubeKeywordMetricRepository;

    @Transactional
    public void calculateNaverRanking() {
        List<SourceType> naverSources = List.of(
                SourceType.BLOG, SourceType.NEWS, SourceType.CAFE, SourceType.SHOPPING
        );

        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummariesBySourceTypes(
                        naverSources, PageRequest.of(0, 20)
                );

        calculateAndSave(RankingType.NAVER, keywords);
    }

    @Transactional
    public void calculateYoutubeRanking() {
        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummariesBySourceTypes(
                        List.of(SourceType.YOUTUBE), PageRequest.of(0, 30)
                );

        if (keywords.isEmpty()) {
            return;
        }

        int maxFrequency = keywords.get(0).getTotalFrequency();

        List<Long> engagementScores = keywords.stream()
                .map(k -> youtubeKeywordMetricRepository.calculateEngagementScore(k.getKeyword()))
                .toList();

        long maxEngagement = engagementScores.stream().mapToLong(Long::longValue).max().orElse(1L);

        LocalDateTime rankedAt = LocalDateTime.now();

        record KeywordScore(String keyword, double frequencyScore, double engagementScore, double finalScore) {}

        List<KeywordScore> scored = new ArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            double freqScore = ((double) keywords.get(i).getTotalFrequency() / maxFrequency) * 100;
            double engScore = maxEngagement > 0 ? ((double) engagementScores.get(i) / maxEngagement) * 100 : 0.0;
            double finalScore = freqScore * 0.5 + engScore * 0.5;
            scored.add(new KeywordScore(keywords.get(i).getKeyword(), freqScore, engScore, finalScore));
        }

        scored.sort(Comparator.comparingDouble(KeywordScore::finalScore).reversed());

        for (int i = 0; i < scored.size(); i++) {
            KeywordScore s = scored.get(i);
            trendKeywordRankingRepository.save(new TrendKeywordRanking(
                    RankingType.YOUTUBE,
                    s.keyword(),
                    s.frequencyScore(),
                    s.engagementScore(),
                    s.finalScore(),
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

    private void calculateAndSave(RankingType rankingType, List<KeywordFrequencySummary> keywords) {
        if (keywords.isEmpty()) {
            return;
        }

        int maxFrequency = keywords.get(0).getTotalFrequency();
        LocalDateTime rankedAt = LocalDateTime.now();

        int rank = 1;

        for (KeywordFrequencySummary keyword : keywords) {
            double frequencyScore = calculateFrequencyScore(keyword.getTotalFrequency(), maxFrequency);

            TrendKeywordRanking ranking = new TrendKeywordRanking(
                    rankingType,
                    keyword.getKeyword(),
                    frequencyScore,
                    0.0,
                    frequencyScore,
                    rank,
                    rankedAt
            );

            trendKeywordRankingRepository.save(ranking);
            rank++;
        }
    }

    private double calculateFrequencyScore(int frequency, int maxFrequency) {
        return ((double) frequency / maxFrequency) * 100;
    }
}
