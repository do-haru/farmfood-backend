package com.riansoft.farmfood.repository.metric;

import com.riansoft.farmfood.domain.metric.KeywordDailySearchEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KeywordDailySearchEstimateRepository extends JpaRepository<KeywordDailySearchEstimate, Long> {

    Optional<KeywordDailySearchEstimate> findByKeywordAndSearchDate(String keyword, LocalDate searchDate);

    List<KeywordDailySearchEstimate> findByKeywordOrderBySearchDateAsc(String keyword);

    @Query("SELECT MAX(e.searchDate) FROM KeywordDailySearchEstimate e")
    Optional<LocalDate> findLatestSearchDate();

    // 기간 내 키워드별 검색수 합계 (랭킹 계산용)
    @Query("""
        SELECT e.keyword, SUM(e.searchCount) as totalCount
        FROM KeywordDailySearchEstimate e
        WHERE e.searchDate >= :from AND e.searchDate <= :to
        GROUP BY e.keyword
        ORDER BY totalCount DESC
        """)
    List<Object[]> findKeywordSearchCountSumBetween(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    // 전날 vs 전전날 증감률 기준 급상승 키워드 (네이버 급상승용)
    @Query(value = """
        SELECT curr.keyword,
               (curr.search_count - prev.search_count) * 100.0 /
               NULLIF(prev.search_count, 0) AS growth_rate
        FROM keyword_daily_search_estimate curr
        JOIN keyword_daily_search_estimate prev
            ON curr.keyword = prev.keyword
            AND prev.search_date = :prevDate
        WHERE curr.search_date = :latestDate
          AND prev.search_count > 0
        ORDER BY growth_rate DESC
        LIMIT 5
        """, nativeQuery = true)
    List<RisingKeywordSummary> findTop5RisingKeywordsByDate(
            @Param("latestDate") LocalDate latestDate,
            @Param("prevDate") LocalDate prevDate
    );
}
