package com.riansoft.farmfood.service.search;

import com.riansoft.farmfood.domain.search.SearchContent;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.external.naver.NaverSearchClient;
import com.riansoft.farmfood.external.naver.dto.NaverSearchItem;
import com.riansoft.farmfood.external.naver.dto.NaverSearchResponse;
import com.riansoft.farmfood.external.youtube.YoutubeSearchClient;
import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchItem;
import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchResponse;
import com.riansoft.farmfood.repository.search.SearchContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchContentService {

    private final NaverSearchClient naverSearchClient;
    private final YoutubeSearchClient youtubeSearchClient;
    private final SearchContentRepository searchContentRepository;

    private final FoodSeedKeywordProvider foodSeedKeywordProvider;

    public void collectBlogSearchContents(String searchKeyword) {
        NaverSearchResponse response = naverSearchClient.searchBlog(searchKeyword);

        for (NaverSearchItem item : response.getItems()) {
            if (isDuplicate(SourceType.BLOG, item.getLink())) {
                continue;
            }

            SearchContent content = new SearchContent(
                    SourceType.BLOG,
                    searchKeyword,
                    item.getTitle(),
                    item.getDescription(),
                    item.getLink(),
                    parsePostdate(item.getPostdate()),
                    LocalDateTime.now()
            );

            searchContentRepository.save(content);
        }
    }

    public void collectNewsSearchContents(String searchKeyword) {
        NaverSearchResponse response = naverSearchClient.searchNews(searchKeyword);

        for (NaverSearchItem item : response.getItems()) {
            if (isDuplicate(SourceType.NEWS, item.getLink())) {
                continue;
            }

            SearchContent content = new SearchContent(
                    SourceType.NEWS,
                    searchKeyword,
                    item.getTitle(),
                    item.getDescription(),
                    item.getLink(),
                    parsePubDate(item.getPubDate()),
                    LocalDateTime.now()
            );

            searchContentRepository.save(content);
        }
    }

    public void collectCafeSearchContents(String searchKeyword) {
        NaverSearchResponse response = naverSearchClient.searchCafe(searchKeyword);

        for (NaverSearchItem item : response.getItems()) {
            if (isDuplicate(SourceType.CAFE, item.getLink())) {
                continue;
            }

            SearchContent content = new SearchContent(
                    SourceType.CAFE,
                    searchKeyword,
                    item.getTitle(),
                    item.getDescription(),
                    item.getLink(),
                    null,
                    LocalDateTime.now()
            );
            searchContentRepository.save(content);

        }
    }

    public void collectShoppingSearchContents(String searchKeyword) {
        NaverSearchResponse response = naverSearchClient.searchShopping(searchKeyword);

        for (NaverSearchItem item : response.getItems()) {
            if (isDuplicate(SourceType.SHOPPING, item.getLink())) {
                continue;
            }

            SearchContent content = new SearchContent(
                    SourceType.SHOPPING,
                    searchKeyword,
                    item.getTitle(),
                    buildShoppingDescription(item),
                    item.getLink(),
                    null,
                    LocalDateTime.now()
            );

            searchContentRepository.save(content);
        }
    }

    public void collectYoutubeSearchContents(String searchKeyword) {
        YoutubeSearchResponse response = youtubeSearchClient.search(searchKeyword);

        if (response == null || response.getItems() == null) {
            return;
        }

        for (YoutubeSearchItem item : response.getItems()) {
            String link = "https://www.youtube.com/watch?v=" + item.getId().getVideoId();

            if (isDuplicate(SourceType.YOUTUBE, link)) {
                continue;
            }

            SearchContent content = new SearchContent(
                    SourceType.YOUTUBE,
                    searchKeyword,
                    item.getSnippet().getTitle(),
                    item.getSnippet().getDescription(),
                    link,
                    parsePublishedAt(item.getSnippet().getPublishedAt()),
                    LocalDateTime.now()
            );

            searchContentRepository.save(content);
        }
    }

    public void collectAllSearchContents(String keyword) {
        collectBlogSearchContents(keyword);
        collectNewsSearchContents(keyword);
        collectCafeSearchContents(keyword);
        collectShoppingSearchContents(keyword);
        collectYoutubeSearchContents(keyword);
    }

    @Transactional
    public void collectSeedKeywordSearchContents() {
        searchContentRepository.deleteAll();

        List<String> seedKeywords = foodSeedKeywordProvider.getSeedKeywords();

        for (String seedKeyword : seedKeywords) {
            collectAllSearchContents(seedKeyword);
            sleep(1000);
        }
    }

    private LocalDate parsePostdate(String postdate) {
        if (postdate == null || postdate.isBlank()) {
            return null;
        }
        return LocalDate.parse(postdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private LocalDate parsePublishedAt(String publishedAt) {
        if (publishedAt == null || publishedAt.isBlank()) {
            return null;
        }
        return ZonedDateTime.parse(publishedAt, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
    }

    private LocalDate parsePubDate(String pubDate) {
        if (pubDate == null || pubDate.isBlank()) {
            return null;
        }

        return ZonedDateTime.parse(
                pubDate,
                DateTimeFormatter.RFC_1123_DATE_TIME
        ).toLocalDate();
    }

    private String buildShoppingDescription(NaverSearchItem item) {
        String category = Stream.of(
                        item.getCategory1(),
                        item.getCategory2(),
                        item.getCategory3(),
                        item.getCategory4()
                )
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.joining(" > "));

        return "판매처: " + nullToBlank(item.getMallName())
                + " / 브랜드: " + nullToBlank(item.getBrand())
                + " / 카테고리: " + category;
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("수집 대기 중 인터럽트 발생", e);
        }
    }

    private boolean isDuplicate(SourceType sourceType, String link) {
        if (link == null || link.isBlank()) {
            return false;
        }

        return searchContentRepository.existsBySourceTypeAndLink(sourceType, link);
    }
}
