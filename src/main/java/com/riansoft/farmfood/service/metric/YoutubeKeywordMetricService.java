package com.riansoft.farmfood.service.metric;

import com.riansoft.farmfood.domain.metric.YoutubeKeywordMetric;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.external.youtube.YoutubeSearchClient;
import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchItem;
import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchResponse;
import com.riansoft.farmfood.external.youtube.dto.YoutubeVideoItem;
import com.riansoft.farmfood.external.youtube.dto.YoutubeVideoStatistics;
import com.riansoft.farmfood.external.youtube.dto.YoutubeVideoStatisticsResponse;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.keyword.KeywordFrequencySummary;
import com.riansoft.farmfood.repository.metric.YoutubeKeywordMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeKeywordMetricService {

    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final YoutubeKeywordMetricRepository youtubeKeywordMetricRepository;
    private final YoutubeSearchClient youtubeSearchClient;

    @Transactional
    public void collectMetrics() {
        List<KeywordFrequencySummary> keywords =
                extractedKeywordRepository.findKeywordFrequencySummariesBySourceTypes(
                        List.of(SourceType.YOUTUBE), PageRequest.of(0, 50)
                );

        for (KeywordFrequencySummary keyword : keywords) {
            collectMetricForKeyword(keyword.getKeyword());
            sleep(500);
        }
    }

    private void collectMetricForKeyword(String keyword) {
        YoutubeSearchResponse searchResponse = youtubeSearchClient.search(keyword, 10);

        if (searchResponse == null || searchResponse.getItems() == null) {
            return;
        }

        List<String> videoIds = searchResponse.getItems().stream()
                .map(YoutubeSearchItem::getId)
                .filter(id -> id != null && id.getVideoId() != null)
                .map(id -> id.getVideoId())
                .toList();

        if (videoIds.isEmpty()) {
            return;
        }

        YoutubeVideoStatisticsResponse statisticsResponse =
                youtubeSearchClient.getVideoStatistics(videoIds);

        if (statisticsResponse == null || statisticsResponse.getItems() == null) {
            return;
        }

        youtubeKeywordMetricRepository.deleteByKeyword(keyword);

        for (YoutubeVideoItem item : statisticsResponse.getItems()) {
            YoutubeVideoStatistics stats = item.getStatistics();

            if (stats == null) {
                continue;
            }

            youtubeKeywordMetricRepository.save(new YoutubeKeywordMetric(
                    keyword,
                    item.getId(),
                    parseLong(stats.getViewCount()),
                    parseLong(stats.getLikeCount()),
                    parseLong(stats.getCommentCount()),
                    LocalDateTime.now()
            ));
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("유튜브 지표 수집 대기 중 인터럽트 발생", e);
        }
    }
}
