package com.riansoft.farmfood.domain.metric;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class KeywordTrendMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String timeUnit;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    public KeywordTrendMetric(String keyword, MetricType metricType, String period, Double value, String timeUnit, LocalDateTime collectedAt) {
        this.keyword = keyword;
        this.metricType = metricType;
        this.period = period;
        this.value = value;
        this.timeUnit = timeUnit;
        this.collectedAt = collectedAt;
    }
}
