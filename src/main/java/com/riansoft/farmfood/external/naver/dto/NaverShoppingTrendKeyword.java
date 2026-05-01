package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverShoppingTrendKeyword {

    private String name;

    private List<String> param;

    public NaverShoppingTrendKeyword(String name, List<String> param) {
        this.name = name;
        this.param = param;
    }
}
