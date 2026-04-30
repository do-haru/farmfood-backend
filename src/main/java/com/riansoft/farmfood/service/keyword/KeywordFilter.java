package com.riansoft.farmfood.service.keyword;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class KeywordFilter {

    private static final Set<String> STOPWORDS = Set.of(
            "추천", "상품", "판매", "구매", "가격", "배송",
            "정보", "관련", "오늘", "이번", "최저가",
            "리뷰", "후기", "할인", "무료", "행사",
            "네이버", "블로그", "뉴스", "카페", "브랜드", "판매처", "카테고리", "식품", "산지",
            "농산물", "수산물", "해산물", "직송", "푸드", "전통", "암", "신선", "어머니"
    );

    public boolean isValid(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }

        if (STOPWORDS.contains(keyword)) {
            return false;
        }

        if (keyword.matches("^[0-9]+$")) {
            return false;
        }

        return true;
    }
}
