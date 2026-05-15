package com.riansoft.farmfood.repository.ranking;

import com.riansoft.farmfood.domain.ranking.PeriodType;
import com.riansoft.farmfood.domain.ranking.RankingType;
import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrendKeywordRankingRepository extends JpaRepository<TrendKeywordRanking, Long> {

    @Query("""
        SELECT t FROM TrendKeywordRanking t
        WHERE t.rankingType = :rankingType
          AND t.periodType = :periodType
          AND t.rankedAt = (
            SELECT MAX(t2.rankedAt) FROM TrendKeywordRanking t2
            WHERE t2.rankingType = :rankingType
              AND t2.periodType = :periodType
          )
        ORDER BY t.rank ASC
        """)
    List<TrendKeywordRanking> findLatestByRankingTypeAndPeriodType(
            @Param("rankingType") RankingType rankingType,
            @Param("periodType") PeriodType periodType
    );

    @Query("""
        SELECT t FROM TrendKeywordRanking t
        WHERE t.rankingType = :rankingType
          AND t.periodType = :periodType
          AND t.rankedAt = (
            SELECT MAX(t2.rankedAt) FROM TrendKeywordRanking t2
            WHERE t2.rankingType = :rankingType
              AND t2.periodType = :periodType
              AND t2.rankedAt < (
                SELECT MAX(t3.rankedAt) FROM TrendKeywordRanking t3
                WHERE t3.rankingType = :rankingType
                  AND t3.periodType = :periodType
              )
          )
        ORDER BY t.rank ASC
        """)
    List<TrendKeywordRanking> findPreviousByRankingTypeAndPeriodType(
            @Param("rankingType") RankingType rankingType,
            @Param("periodType") PeriodType periodType
    );

    void deleteByRankingTypeAndPeriodType(RankingType rankingType, PeriodType periodType);
}
