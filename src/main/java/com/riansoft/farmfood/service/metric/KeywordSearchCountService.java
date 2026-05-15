package com.riansoft.farmfood.service.metric;

import com.riansoft.farmfood.domain.metric.KeywordDailySearchEstimate;
import com.riansoft.farmfood.domain.metric.KeywordSearchCount;
import com.riansoft.farmfood.domain.metric.KeywordTrendMetric;
import com.riansoft.farmfood.domain.metric.MetricType;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.external.naver.NaverAdSearchClient;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.keyword.KeywordFrequencySummary;
import com.riansoft.farmfood.repository.metric.KeywordDailySearchEstimateRepository;
import com.riansoft.farmfood.repository.metric.KeywordSearchCountRepository;
import com.riansoft.farmfood.repository.metric.KeywordTrendMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordSearchCountService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final KeywordSearchCountRepository keywordSearchCountRepository;
    private final KeywordDailySearchEstimateRepository keywordDailySearchEstimateRepository;
    private final KeywordTrendMetricRepository keywordTrendMetricRepository;
    private final NaverAdSearchClient naverAdSearchClient;

    private static final List<SourceType> NAVER_SOURCE_TYPES =
            List.of(SourceType.BLOG, SourceType.NEWS, SourceType.CAFE, SourceType.SHOPPING);

    @Transactional
    public void collectMonthlySearchCounts() {
        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummariesBySourceTypes(
                        NAVER_SOURCE_TYPES, PageRequest.of(0, 50)
                );

        String yearMonth = YearMonth.now().toString(); // "2025-05"

        for (KeywordFrequencySummary summary : keywords) {
            String keyword = summary.getKeyword();
            try {
                long searchCount = naverAdSearchClient.getMonthlySearchCount(keyword);

                // SA 월간 검색수 저장
                keywordSearchCountRepository
                        .findByKeywordAndDate(keyword, yearMonth)
                        .ifPresentOrElse(
                                existing -> existing.update(searchCount, LocalDateTime.now()),
                                () -> keywordSearchCountRepository.save(
                                        new KeywordSearchCount(keyword, yearMonth, searchCount, LocalDateTime.now())
                                )
                        );

                // DataLab 지수로 일별 추정 검색수 역산 후 저장
                saveDailySearchEstimates(keyword, yearMonth, searchCount);

                sleep(300);
            } catch (Exception e) {
                log.warn("월간 검색수 수집 실패 - keyword: {}, error: {}", keyword, e.getMessage());
            }
        }
    }

    private void saveDailySearchEstimates(String keyword, String yearMonth, long monthlySearchCount) {
        List<KeywordTrendMetric> metrics = keywordTrendMetricRepository
                .findByKeywordAndMetricTypeOrderByPeriodAsc(keyword, MetricType.SHOPPING_TREND);

        // 이번 달 DataLab 지수만 필터링
        List<KeywordTrendMetric> currentMonthMetrics = metrics.stream()
                .filter(m -> m.getPeriod().startsWith(yearMonth))
                .toList();

        double monthlyIndexSum = currentMonthMetrics.stream()
                .mapToDouble(KeywordTrendMetric::getValue)
                .sum();

        if (monthlyIndexSum <= 0) return;

        double scaleFactor = monthlySearchCount / monthlyIndexSum;

        LocalDateTime now = LocalDateTime.now();

        for (KeywordTrendMetric metric : metrics) {
            long estimated = Math.round(metric.getValue() * scaleFactor);
            LocalDate searchDate = LocalDate.parse(metric.getPeriod());

            keywordDailySearchEstimateRepository
                    .findByKeywordAndSearchDate(keyword, searchDate)
                    .ifPresentOrElse(
                            existing -> existing.update(estimated, now),
                            () -> keywordDailySearchEstimateRepository.save(
                                    new KeywordDailySearchEstimate(keyword, searchDate, estimated, now)
                            )
                    );
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("월간 검색수 수집 대기 중 인터럽트 발생", e);
        }
    }
}
