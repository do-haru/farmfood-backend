package com.riansoft.farmfood.domain.ranking;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TrendKeywordRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RankingType rankingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    @Column(nullable = false)

    private String keyword;

    @Column(nullable = false)
    private Long searchCount;

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private LocalDateTime rankedAt;

    public TrendKeywordRanking(
            RankingType rankingType,
            PeriodType periodType,
            String keyword,
            Long searchCount,
            Integer rank,
            LocalDateTime rankedAt
    ) {
        this.rankingType = rankingType;
        this.periodType = periodType;
        this.keyword = keyword;
        this.searchCount = searchCount;
        this.rank = rank;
        this.rankedAt = rankedAt;
    }
}
