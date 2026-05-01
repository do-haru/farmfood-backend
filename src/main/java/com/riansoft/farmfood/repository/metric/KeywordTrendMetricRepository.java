package com.riansoft.farmfood.repository.metric;

import com.riansoft.farmfood.domain.metric.KeywordTrendMetric;
import com.riansoft.farmfood.domain.metric.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
