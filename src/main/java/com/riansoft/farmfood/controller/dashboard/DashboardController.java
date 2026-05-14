package com.riansoft.farmfood.controller.dashboard;

import com.riansoft.farmfood.controller.dashboard.dto.DashboardRankingResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardRisingKeywordResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardSearchContentResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardShoppingTrendResponse;
import com.riansoft.farmfood.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/rankings")
    public List<DashboardRankingResponse> getTrendKeywordRankings() {
        return dashboardService.getTrendKeywordRankings();
    }

    @GetMapping("/rankings/naver")
    public List<DashboardRankingResponse> getNaverRankings() {
        return dashboardService.getNaverRankings();
    }

    @GetMapping("/rankings/youtube")
    public List<DashboardRankingResponse> getYoutubeRankings() {
        return dashboardService.getYoutubeRankings();
    }

    @GetMapping("/rising-keywords/naver")
    public List<DashboardRisingKeywordResponse> getNaverRisingKeywords() {
        return dashboardService.getNaverRisingKeywords();
    }

    @GetMapping("/rising-keywords/youtube")
    public List<DashboardRisingKeywordResponse> getYoutubeRisingKeywords() {
        return dashboardService.getYoutubeRisingKeywords();
    }

    @GetMapping("/keywords/{keyword}/blog-contents")
    public List<DashboardSearchContentResponse> getNaverBlogContents(@PathVariable String keyword) {
        return dashboardService.getNaverBlogContents(keyword);
    }

    @GetMapping("/keywords/{keyword}/shopping-trends")
    public List<DashboardShoppingTrendResponse> getShoppingTrends(@PathVariable String keyword) {
        return dashboardService.getShoppingTrends(keyword);
    }
}
