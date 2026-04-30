package com.riansoft.farmfood.service.search;

import com.riansoft.farmfood.domain.search.SearchContent;
import com.riansoft.farmfood.domain.search.SourceType;
import com.riansoft.farmfood.external.naver.NaverSearchClient;
import com.riansoft.farmfood.external.naver.dto.NaverSearchItem;
import com.riansoft.farmfood.external.naver.dto.NaverSearchResponse;
import com.riansoft.farmfood.repository.search.SearchContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SearchContentService {

    private final NaverSearchClient naverSearchClient;
    private final SearchContentRepository searchContentRepository;

    public void collectBlogSearchContents(String searchKeyword) {
        NaverSearchResponse response = naverSearchClient.searchBlog(searchKeyword);

        for (NaverSearchItem item : response.getItems()) {
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

    private LocalDate parsePostdate(String postdate) {
        if (postdate == null || postdate.isBlank()) {
            return null;
        }
        return LocalDate.parse(postdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
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
}
