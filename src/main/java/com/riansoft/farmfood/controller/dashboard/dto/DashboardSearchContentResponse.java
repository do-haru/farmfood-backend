package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.external.naver.dto.NaverSearchItem;
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

    public DashboardSearchContentResponse(String title, String description, String link, LocalDate publishedAt) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedAt = publishedAt;
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
