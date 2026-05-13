package com.riansoft.farmfood.service.ranking;

import com.riansoft.farmfood.domain.ranking.RankingType;
import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.keyword.KeywordFrequencySummary;
import org.springframework.data.domain.PageRequest;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendKeywordRankingService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final TrendKeywordRankingRepository trendKeywordRankingRepository;

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
                        List.of(SourceType.YOUTUBE), PageRequest.of(0, 20)
                );

        calculateAndSave(RankingType.YOUTUBE, keywords);
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

        trendKeywordRankingRepository.deleteByRankingType(rankingType);

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
