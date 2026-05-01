package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverShoppingTrendResponse {

    private String startDate;

    private String endDate;

    private String timeUnit;

    private List<NaverShoppingTrendResult> results;
}
