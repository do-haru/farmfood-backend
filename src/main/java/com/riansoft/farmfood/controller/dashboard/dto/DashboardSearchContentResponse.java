package com.riansoft.farmfood.controller.dashboard.dto;

import com.riansoft.farmfood.external.naver.dto.NaverSearchItem;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
}
