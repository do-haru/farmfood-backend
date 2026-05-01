package com.riansoft.farmfood.service.ranking;

import com.riansoft.farmfood.domain.keyword.ExtractedKeyword;
import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
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
    public void calculateRanking() {
        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummaries(PageRequest.of(0, 20));

        if (keywords.isEmpty()) {
            return;
        }

        int maxFrequency = keywords.get(0).getTotalFrequency();
        LocalDateTime rankedAt = LocalDateTime.now();

        trendKeywordRankingRepository.deleteAll();

        int rank = 1;

        for (KeywordFrequencySummary keyword : keywords) {
            double frequencyScore = calculateFrequencyScore(
                    keyword.getTotalFrequency(),
                    maxFrequency
            );

            double trendScore = 0.0;
            double finalScore = frequencyScore;

            TrendKeywordRanking ranking = new TrendKeywordRanking(
                    keyword.getKeyword(),
                    frequencyScore,
                    trendScore,
                    finalScore,
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
