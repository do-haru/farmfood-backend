package com.riansoft.farmfood.controller.dashboard;

import com.riansoft.farmfood.controller.dashboard.dto.DashboardRankingResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardRisingKeywordResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardSearchContentResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardShoppingTrendResponse;
import com.riansoft.farmfood.controller.dashboard.dto.DashboardYoutubeReactionResponse;
import com.riansoft.farmfood.domain.ranking.PeriodType;
import com.riansoft.farmfood.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/rankings/naver")
    public List<DashboardRankingResponse> getNaverRankings(
            @RequestParam(defaultValue = "DAILY") PeriodType periodType
    ) {
        return dashboardService.getNaverRankings(periodType);
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

    @GetMapping("/keywords/{keyword}/news-contents")
    public List<DashboardSearchContentResponse> getNaverNewsContents(@PathVariable String keyword) {
        return dashboardService.getNaverNewsContents(keyword);
    }

    @GetMapping("/keywords/{keyword}/cafe-contents")
    public List<DashboardSearchContentResponse> getNaverCafeContents(@PathVariable String keyword) {
        return dashboardService.getNaverCafeContents(keyword);
    }

    @GetMapping("/keywords/{keyword}/shopping-contents")
    public List<DashboardSearchContentResponse> getNaverShoppingContents(@PathVariable String keyword) {
        return dashboardService.getNaverShoppingContents(keyword);
    }

    @GetMapping("/keywords/{keyword}/youtube-reaction")
    public DashboardYoutubeReactionResponse getYoutubeReaction(@PathVariable String keyword) {
        return dashboardService.getYoutubeReaction(keyword);
    }

    @GetMapping("/keywords/{keyword}/youtube-contents")
    public List<DashboardSearchContentResponse> getYoutubeContents(@PathVariable String keyword) {
        return dashboardService.getYoutubeContents(keyword);
    }

    @GetMapping("/keywords/{keyword}/shopping-trends")
    public List<DashboardShoppingTrendResponse> getShoppingTrends(@PathVariable String keyword) {
        return dashboardService.getShoppingTrends(keyword);
    }
}
