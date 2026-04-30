package com.riansoft.farmfood.external.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverSearchItem {

    private String title;

    private String link;

    private String description;

    // 블로그 검색 API 날짜 필드
    private String postdate;

    // 뉴스 검색 API 날짜 필드
    private String pubDate;

    // 쇼핑 검색 API 필드
    private String mallName;

    private String brand;

    private String category1;

    private String category2;

    private String category3;

    private String category4;
}
