package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NaverAdKeywordResponse {
    private List<NaverAdKeywordItem> keywordList;
}
