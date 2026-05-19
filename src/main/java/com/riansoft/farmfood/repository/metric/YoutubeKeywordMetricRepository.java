package com.riansoft.farmfood.repository.metric;

import com.riansoft.farmfood.domain.metric.YoutubeKeywordMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YoutubeKeywordMetricRepository extends JpaRepository<YoutubeKeywordMetric, Long> {

    @Query("""
        select m.viewCount + m.likeCount * 10 + m.commentCount * 5
        from YoutubeKeywordMetric m
        where m.keyword = :keyword
          and m.collectedAt = (
              select max(m2.collectedAt) from YoutubeKeywordMetric m2 where m2.keyword = :keyword
          )
        """)
    Long calculateEngagementScore(@Param("keyword") String keyword);

    @Query("""
        select new com.riansoft.farmfood.repository.metric.YoutubeReactionSummary(
            sum(m.viewCount), sum(m.likeCount), sum(m.commentCount)
        )
        from YoutubeKeywordMetric m
        where m.keyword = :keyword
          and m.collectedAt = (
              select max(m2.collectedAt) from YoutubeKeywordMetric m2 where m2.keyword = :keyword
          )
        """)
    java.util.Optional<YoutubeReactionSummary> findReactionSummaryByKeyword(@Param("keyword") String keyword);

    @Query(value = """
        WITH latest AS (
            SELECT keyword, MAX(collected_at) AS max_time
            FROM youtube_keyword_metric
            GROUP BY keyword
        ),
        prev AS (
            SELECT m.keyword, MAX(m.collected_at) AS prev_time
            FROM youtube_keyword_metric m
            JOIN latest l ON m.keyword = l.keyword
            WHERE m.collected_at < l.max_time
            GROUP BY m.keyword
        )
        SELECT l_m.keyword,
               (l_m.view_count + l_m.like_count * 10 + l_m.comment_count * 5
                - p_m.view_count - p_m.like_count * 10 - p_m.comment_count * 5) * 100.0
               / NULLIF(p_m.view_count + p_m.like_count * 10 + p_m.comment_count * 5, 0) AS growth_rate
        FROM youtube_keyword_metric l_m
        JOIN latest l ON l_m.keyword = l.keyword AND l_m.collected_at = l.max_time
        JOIN prev p ON l_m.keyword = p.keyword
        JOIN youtube_keyword_metric p_m ON p_m.keyword = p.keyword AND p_m.collected_at = p.prev_time
        WHERE (p_m.view_count + p_m.like_count * 10 + p_m.comment_count * 5) > 0
        ORDER BY growth_rate DESC
        LIMIT 5
        """, nativeQuery = true)
    List<RisingKeywordSummary> findTop5YoutubeRisingKeywords();
}
