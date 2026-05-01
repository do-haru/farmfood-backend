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

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private Double frequencyScore;

    @Column(nullable = false)
    private Double trendScore;

    @Column(nullable = false)
    private Double finalScore;

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private LocalDateTime rankedAt;

    public TrendKeywordRanking(
            String keyword,
            Double frequencyScore,
            Double trendScore,
            Double finalScore,
            Integer rank,
            LocalDateTime rankedAt
    ) {
        this.keyword = keyword;
        this.frequencyScore = frequencyScore;
        this.trendScore = trendScore;
        this.finalScore = finalScore;
        this.rank = rank;
        this.rankedAt = rankedAt;
    }



}
