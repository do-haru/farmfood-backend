package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverShoppingTrendResult {

    private String title;

    private List<String> category;

    private List<NaverShoppingTrendData> data;
}
