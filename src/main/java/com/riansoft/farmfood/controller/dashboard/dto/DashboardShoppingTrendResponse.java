package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.domain.metric.KeywordDailySearchEstimate;
import com.riansoft.farmfood.domain.metric.KeywordTrendMetric;
import lombok.Getter;

@Getter
public class DashboardShoppingTrendResponse {

    private final String period;

    private final Double value;

    private final String timeUnit;

    public DashboardShoppingTrendResponse(String period, Double value, String timeUnit) {
        this.period = period;
        this.value = value;
        this.timeUnit = timeUnit;
    }

    public static DashboardShoppingTrendResponse from(KeywordTrendMetric metric) {
        return new DashboardShoppingTrendResponse(
                metric.getPeriod(),
                metric.getValue(),
                metric.getTimeUnit()
        );
    }

    public static DashboardShoppingTrendResponse fromScaled(KeywordTrendMetric metric, double scaleFactor) {
        long scaled = Math.round(metric.getValue() * scaleFactor);
        return new DashboardShoppingTrendResponse(
                metric.getPeriod(),
                (double) scaled,
                metric.getTimeUnit()
        );
    }

    public static DashboardShoppingTrendResponse fromEstimate(KeywordDailySearchEstimate estimate) {
        return new DashboardShoppingTrendResponse(
                estimate.getSearchDate().toString(),
                (double) estimate.getSearchCount(),
                "date"
        );
    }
}
