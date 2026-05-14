package com.riansoft.farmfood.repository.metric;

import com.riansoft.farmfood.domain.metric.KeywordTrendMetric;
import com.riansoft.farmfood.domain.metric.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeywordTrendMetricRepository extends JpaRepository<KeywordTrendMetric, Long> {

    Optional<KeywordTrendMetric> findByKeywordAndMetricTypeAndPeriodAndTimeUnit(
            String keyword,
            MetricType metricType,
            String period,
            String timeUnit
    );

    List<KeywordTrendMetric> findByKeywordAndMetricTypeOrderByPeriodAsc(
            String keyword,
            MetricType metricType
    );

    @Query(value = """
        SELECT
            keyword,
            (AVG(CASE WHEN period >= :recentStart THEN value END) -
             AVG(CASE WHEN period < :recentStart AND period >= :prevStart THEN value END)) /
            NULLIF(AVG(CASE WHEN period < :recentStart AND period >= :prevStart THEN value END), 0) * 100
            AS growth_rate
        FROM keyword_trend_metric
        WHERE metric_type = 'SHOPPING_TREND'
          AND period >= :prevStart
        GROUP BY keyword
        HAVING AVG(CASE WHEN period < :recentStart AND period >= :prevStart THEN value END) > 0
        ORDER BY growth_rate DESC
        LIMIT 5
        """, nativeQuery = true)
    List<RisingKeywordSummary> findTop5RisingKeywords(
            @Param("recentStart") String recentStart,
            @Param("prevStart") String prevStart
    );
}
