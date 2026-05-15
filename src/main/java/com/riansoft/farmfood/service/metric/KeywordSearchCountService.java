package com.riansoft.farmfood.service.metric;

import com.riansoft.farmfood.domain.metric.KeywordSearchCount;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.external.naver.NaverAdSearchClient;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.keyword.KeywordFrequencySummary;
import com.riansoft.farmfood.repository.metric.KeywordSearchCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordSearchCountService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final KeywordSearchCountRepository keywordSearchCountRepository;
    private final NaverAdSearchClient naverAdSearchClient;

    private static final List<SourceType> NAVER_SOURCE_TYPES =
            List.of(SourceType.BLOG, SourceType.NEWS, SourceType.CAFE, SourceType.SHOPPING);

    @Transactional
    public void collectMonthlySearchCounts() {
        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummariesBySourceTypes(
                        NAVER_SOURCE_TYPES, PageRequest.of(0, 50)
                );

        String date = YearMonth.now().toString(); // "2025-05"

        for (KeywordFrequencySummary summary : keywords) {
            String keyword = summary.getKeyword();
            try {
                long searchCount = naverAdSearchClient.getMonthlySearchCount(keyword);

                keywordSearchCountRepository
                        .findByKeywordAndDate(keyword, date)
                        .ifPresentOrElse(
                                existing -> existing.update(searchCount, LocalDateTime.now()),
                                () -> keywordSearchCountRepository.save(
                                        new KeywordSearchCount(keyword, date, searchCount, LocalDateTime.now())
                                )
                        );

                sleep(300);
            } catch (Exception e) {
                log.warn("월간 검색수 수집 실패 - keyword: {}, error: {}", keyword, e.getMessage());
            }
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
