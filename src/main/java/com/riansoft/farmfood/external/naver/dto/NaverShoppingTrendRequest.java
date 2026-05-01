package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverShoppingTrendRequest {

    private String startDate;

    private String endDate;

    private String timeUnit;

    private String category;

    private List<NaverShoppingTrendKeyword> keyword;

    public NaverShoppingTrendRequest(String startDate,
                                     String endDate,
                                     String timeUnit,
                                     String category,
                                     List<NaverShoppingTrendKeyword> keyword) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeUnit = timeUnit;
        this.category = category;
        this.keyword = keyword;
    }
}
