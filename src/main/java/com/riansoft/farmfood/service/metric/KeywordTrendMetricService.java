package com.riansoft.farmfood.service.metric;

import com.riansoft.farmfood.domain.keyword.ExtractedKeyword;
import com.riansoft.farmfood.domain.metric.KeywordTrendMetric;
import com.riansoft.farmfood.domain.metric.MetricType;
import com.riansoft.farmfood.external.naver.NaverShoppingTrendClient;
import com.riansoft.farmfood.external.naver.dto.NaverShoppingTrendData;
import com.riansoft.farmfood.external.naver.dto.NaverShoppingTrendResponse;
import com.riansoft.farmfood.external.naver.dto.NaverShoppingTrendResult;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.metric.KeywordTrendMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordTrendMetricService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final KeywordTrendMetricRepository keywordTrendMetricRepository;
    private final NaverShoppingTrendClient naverShoppingTrendClient;

    @Transactional
    public void collectShoppingTrendMetrics() {
        List<ExtractedKeyword> keywords = extractedKeywordRepository.findTop20ByOrderByFrequencyDesc();

        for (ExtractedKeyword extractedKeyword : keywords) {
            String keyword = extractedKeyword.getKeyword();

            NaverShoppingTrendResponse response = naverShoppingTrendClient.getShoppingTrend(keyword);

            if (response == null || response.getResults() == null) {
                continue;
            }

            for (NaverShoppingTrendResult result : response.getResults()) {
                saveShoppingTrendMetric(result, response.getTimeUnit());
            }

            sleep(500);
        }
    }

    private void saveShoppingTrendMetric(NaverShoppingTrendResult result, String timeUnit) {
        if (result.getData() == null) {
            return;
        }

        for (NaverShoppingTrendData data : result.getData()) {
            keywordTrendMetricRepository
                    .findByKeywordAndMetricTypeAndPeriodAndTimeUnit(
                            result.getTitle(),
                            MetricType.SHOPPING_TREND,
                            data.getPeriod(),
                            timeUnit
                    )
                    .ifPresentOrElse(
                            metric -> {

                            },
                            () -> keywordTrendMetricRepository.save(
                                    new KeywordTrendMetric(
                                            result.getTitle(),
                                            MetricType.SHOPPING_TREND,
                                            data.getPeriod(),
                                            data.getRatio(),
                                            timeUnit,
                                            LocalDateTime.now()
                                    )
                            )
                    );
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("쇼핑 트렌드 수집 대기 중 인터럽트 발생", e);
        }
    }
}
