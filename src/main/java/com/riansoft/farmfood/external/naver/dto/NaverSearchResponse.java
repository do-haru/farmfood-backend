package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverSearchResponse {

    private String lastBuildDate;

    private Integer total;

    private Integer start;

    private Integer display;

    private List<NaverSearchItem> items;
}
