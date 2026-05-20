package com.riansoft.farmfood.service.pipeline;

import com.riansoft.farmfood.service.keyword.ExtractedKeywordService;
import com.riansoft.farmfood.service.metric.KeywordSearchCountService;
import com.riansoft.farmfood.service.metric.KeywordTrendMetricService;
import com.riansoft.farmfood.service.metric.YoutubeKeywordMetricService;
import com.riansoft.farmfood.service.ranking.TrendKeywordRankingService;
import com.riansoft.farmfood.service.search.SearchContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineService {

    private final SearchContentService searchContentService;
    private final ExtractedKeywordService extractedKeywordService;
    private final KeywordTrendMetricService keywordTrendMetricService;
    private final YoutubeKeywordMetricService youtubeKeywordMetricService;
    private final KeywordSearchCountService keywordSearchCountService;
    private final TrendKeywordRankingService trendKeywordRankingService;

    @Async
    public void runAll() {
        log.info("[Pipeline] 시작");

        try {
            runStep("1/4 콘텐츠 수집", searchContentService::collectSeedKeywordSearchContents);
            runStep("2/4 키워드 추출", extractedKeywordService::extractKeywords);
            runStep("3/4 쇼핑 트렌드 지표 수집", keywordTrendMetricService::collectShoppingTrendMetrics);
            runStep("3/4 유튜브 지표 수집", youtubeKeywordMetricService::collectMetrics);
            runStep("3/4 월간 검색수 수집", keywordSearchCountService::collectMonthlySearchCounts);
            runStep("4/4 순위 산출", trendKeywordRankingService::calculateRanking);
            log.info("[Pipeline] 완료");
        } catch (Exception e) {
            log.error("[Pipeline] 중단 - {}", e.getMessage(), e);
        }
    }

    private void runStep(String stepName, Runnable step) {
        log.info("[Pipeline] {} 시작", stepName);
        step.run();
        log.info("[Pipeline] {} 완료", stepName);
    }
}
