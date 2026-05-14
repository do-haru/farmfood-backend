package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.repository.metric.RisingKeywordSummary;
import lombok.Getter;

@Getter
public class DashboardRisingKeywordResponse {

    private final String keyword;

    private final Double growthRate;

    public DashboardRisingKeywordResponse(String keyword, Double growthRate) {
        this.keyword = keyword;
        this.growthRate = growthRate;
    }

    public static DashboardRisingKeywordResponse from(RisingKeywordSummary summary) {
        return new DashboardRisingKeywordResponse(
                summary.getKeyword(),
                summary.getGrowthRate()
        );
    }
}
