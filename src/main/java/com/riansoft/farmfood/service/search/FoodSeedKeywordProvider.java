package com.riansoft.farmfood.service.search;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodSeedKeywordProvider {

    public List<String> getSeedKeywords() {
        return List.of(
                "산지직송",
                "제철 먹거리",
                "농산물",
                "수산물",
                "축산물",
                "로컬푸드",
                "지역 특산물",
                "신선식품",
                "전통 먹거리"
        );
    }
}
