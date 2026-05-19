package com.riansoft.farmfood.repository.metric;

import lombok.Getter;

@Getter
public class YoutubeReactionSummary {

    private final Long viewCount;
    private final Long likeCount;
    private final Long commentCount;

    public YoutubeReactionSummary(Long viewCount, Long likeCount, Long commentCount) {
        this.viewCount = viewCount != null ? viewCount : 0L;
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.commentCount = commentCount != null ? commentCount : 0L;
    }
}
