package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverAdKeywordItem {

    private String relKeyword;

    // 숫자이거나 "< 10" 문자열일 수 있어서 Object로 받음
    private Object monthlyPcQcCnt;
    private Object monthlyMobileQcCnt;

    public long getTotalMonthlySearchCount() {
        return parse(monthlyPcQcCnt) + parse(monthlyMobileQcCnt);
    }

    private long parse(Object value) {
        if (value == null) return 0;
        String str = value.toString().trim();
        if (str.startsWith("<")) return 5; // "< 10" → 5로 처리
        try {
            return Long.parseLong(str.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
