package com.riansoft.farmfood.repository.metric;

import com.riansoft.farmfood.domain.metric.YoutubeKeywordMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface YoutubeKeywordMetricRepository extends JpaRepository<YoutubeKeywordMetric, Long> {

    boolean existsByKeywordAndVideoId(String keyword, String videoId);

    void deleteByKeyword(String keyword);

    @Query("""
        select coalesce(sum(m.viewCount), 0) +
               coalesce(sum(m.likeCount), 0) * 10 +
               coalesce(sum(m.commentCount), 0) * 5
        from YoutubeKeywordMetric m
        where m.keyword = :keyword
        """)
    Long calculateEngagementScore(@Param("keyword") String keyword);
}
