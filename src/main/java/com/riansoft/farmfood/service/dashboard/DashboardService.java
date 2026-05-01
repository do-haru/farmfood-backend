package com.riansoft.farmfood.service.dashboard;

import com.riansoft.farmfood.controller.dashboard.dto.DashboardRankingResponse;
import com.riansoft.farmfood.repository.ranking.TrendKeywordRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TrendKeywordRankingRepository trendKeywordRankingRepository;

    public List<DashboardRankingResponse> getTrendKeywordRankings() {
        return trendKeywordRankingRepository.findTop20ByOrderByRankAsc()
                .stream()
                .map(DashboardRankingResponse::from)
                .toList();
    }
}
