package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.external.naver.dto.NaverSearchItem;
import com.riansoft.farmfood.external.youtube.dto.YoutubeSearchItem;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Getter
public class DashboardSearchContentResponse {

    private final String title;
    private final String description;
    private final String link;
    private final LocalDate publishedAt;
    private final String thumbnailUrl;

    public DashboardSearchContentResponse(String title, String description, String link, LocalDate publishedAt) {
        this(title, description, link, publishedAt, null);
    }

    public DashboardSearchContentResponse(String title, String description, String link, LocalDate publishedAt, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedAt = publishedAt;
        this.thumbnailUrl = thumbnailUrl;
    }

    // 블로그, 카페 (postdate: yyyyMMdd)
    public static DashboardSearchContentResponse from(NaverSearchItem item) {
        LocalDate publishedAt = null;
        if (item.getPostdate() != null && !item.getPostdate().isBlank()) {
            publishedAt = LocalDate.parse(item.getPostdate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        return new DashboardSearchContentResponse(
                item.getTitle(),
                item.getDescription(),
                item.getLink(),
                publishedAt
        );
    }

    // 뉴스 (pubDate: "Thu, 14 May 2026 10:30:00 +0900")
    public static DashboardSearchContentResponse fromNews(NaverSearchItem item) {
        LocalDate publishedAt = null;
        if (item.getPubDate() != null && !item.getPubDate().isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                publishedAt = ZonedDateTime.parse(item.getPubDate(), formatter).toLocalDate();
            } catch (DateTimeParseException ignored) {
            }
        }
        return new DashboardSearchContentResponse(
                item.getTitle(),
                item.getDescription(),
                item.getLink(),
                publishedAt
        );
    }

    // 유튜브 (publishedAt: "2024-01-01T00:00:00Z" ISO-8601)
    public static DashboardSearchContentResponse fromYoutube(YoutubeSearchItem item) {
        LocalDate publishedAt = null;
        String publishedAtStr = item.getSnippet().getPublishedAt();
        if (publishedAtStr != null && !publishedAtStr.isBlank()) {
            try {
                publishedAt = ZonedDateTime.parse(publishedAtStr).toLocalDate();
            } catch (DateTimeParseException ignored) {
            }
        }
        String videoId = item.getId() != null ? item.getId().getVideoId() : null;
        String link = videoId != null ? "https://www.youtube.com/watch?v=" + videoId : null;

        String thumbnailUrl = null;
        if (item.getSnippet().getThumbnails() != null && item.getSnippet().getThumbnails().getMedium() != null) {
            thumbnailUrl = item.getSnippet().getThumbnails().getMedium().getUrl();
        }

        return new DashboardSearchContentResponse(
                item.getSnippet().getTitle(),
                item.getSnippet().getDescription(),
                link,
                publishedAt,
                thumbnailUrl
        );
    }

    // 쇼핑 (날짜 없음, mallName을 description으로 사용)
    public static DashboardSearchContentResponse fromShopping(NaverSearchItem item) {
        String description = item.getMallName() != null && !item.getMallName().isBlank()
                ? item.getMallName()
                : item.getDescription();
        return new DashboardSearchContentResponse(
                item.getTitle(),
                description,
                item.getLink(),
                null
        );
    }
}
