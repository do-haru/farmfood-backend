package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DashboardRankingResponse {

    private final Integer rank;

    private final String keyword;

    private final Long searchCount;

    private final LocalDateTime rankedAt;

    private final Integer rankChange;

    public DashboardRankingResponse(
            Integer rank,
            String keyword,
            Long searchCount,
            LocalDateTime rankedAt,
            Integer rankChange
    ) {
        this.rank = rank;
        this.keyword = keyword;
        this.searchCount = searchCount;
        this.rankedAt = rankedAt;
        this.rankChange = rankChange;
    }

    public static DashboardRankingResponse from(TrendKeywordRanking ranking, Integer rankChange) {
        return new DashboardRankingResponse(
                ranking.getRank(),
                ranking.getKeyword(),
                ranking.getSearchCount(),
                ranking.getRankedAt(),
                rankChange
        );
    }
}
