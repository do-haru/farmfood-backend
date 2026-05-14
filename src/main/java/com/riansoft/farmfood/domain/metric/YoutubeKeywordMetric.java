package com.riansoft.farmfood.domain.metric;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class YoutubeKeywordMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Long commentCount;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    public YoutubeKeywordMetric(
            String keyword,
            Long viewCount,
            Long likeCount,
            Long commentCount,
            LocalDateTime collectedAt
    ) {
        this.keyword = keyword;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.collectedAt = collectedAt;
    }
}
