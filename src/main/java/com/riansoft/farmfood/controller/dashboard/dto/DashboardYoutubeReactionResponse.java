package com.riansoft.farmfood.controller.dashboard.dto;

import lombok.Getter;

@Getter
public class DashboardYoutubeReactionResponse {

    private final Long viewCount;
    private final Long likeCount;
    private final Long commentCount;
    private final Double engagementRate;

    public DashboardYoutubeReactionResponse(Long viewCount, Long likeCount, Long commentCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.engagementRate = viewCount > 0
                ? Math.round((likeCount + commentCount) * 10000.0 / viewCount) / 100.0
                : 0.0;
    }
}
