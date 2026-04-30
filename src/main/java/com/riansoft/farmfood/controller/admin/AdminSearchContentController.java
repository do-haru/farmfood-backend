package com.riansoft.farmfood.controller.admin;

import com.riansoft.farmfood.service.search.SearchContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/search-contents")
@RequiredArgsConstructor
public class AdminSearchContentController {

    private final SearchContentService searchContentService;

    @PostMapping("/collect/blog")
    public String collectBlogSearchContents(@RequestParam String keyword) {
        searchContentService.collectBlogSearchContents(keyword);

        return "블로그 검색 결과 수집 완료: " + keyword;
    }

    @PostMapping("/collect/news")
    public String collectNewsSearchContents(@RequestParam String keyword) {
        searchContentService.collectNewsSearchContents(keyword);

        return "뉴스 검색 결과 수집 완료: " + keyword;
    }

    @PostMapping("/collect/cafe")
    public String collectCafeSearchContents(@RequestParam String keyword) {
        searchContentService.collectCafeSearchContents(keyword);

        return "카페 검색 결과 수집 완료: " + keyword;
    }
}
