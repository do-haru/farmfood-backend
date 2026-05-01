package com.riansoft.farmfood.controller.admin;

import com.riansoft.farmfood.service.ranking.TrendKeywordRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/rankings")
@RequiredArgsConstructor
public class AdminRankingController {

    private final TrendKeywordRankingService trendKeywordRankingService;

    @PostMapping("/calculate")
    public String calculateRanking() {
        trendKeywordRankingService.calculateRanking();

        return "인기 키워드 순위 계산 완료";
    }

}
