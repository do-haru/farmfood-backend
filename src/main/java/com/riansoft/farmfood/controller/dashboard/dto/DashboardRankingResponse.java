package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DashboardRankingResponse {

    private final Integer rank;

    private final String keyword;

    private final Double frequencyScore;

    private final Double trendScore;

    private final Double finalScore;

    private final LocalDateTime rankedAt;

    public DashboardRankingResponse(
            Integer rank,
            String keyword,
            Double frequencyScore,
            Double trendScore,
            Double finalScore,
            LocalDateTime rankedAt
    ) {
        this.rank = rank;
        this.keyword = keyword;
        this.frequencyScore = frequencyScore;
        this.trendScore = trendScore;
        this.finalScore = finalScore;
        this.rankedAt = rankedAt;
    }

    public static DashboardRankingResponse from(TrendKeywordRanking ranking) {
        return new DashboardRankingResponse(
                ranking.getRank(),
                ranking.getKeyword(),
                ranking.getFrequencyScore(),
                ranking.getTrendScore(),
                ranking.getFinalScore(),
                ranking.getRankedAt()
        );
    }
}
