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
}
