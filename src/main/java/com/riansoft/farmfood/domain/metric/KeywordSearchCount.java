package com.riansoft.farmfood.domain.metric;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class KeywordSearchCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    // "2025-05" 형식, date는 SQL 예약어라 따옴표 처리
    @Column(name = "\"date\"", nullable = false)
    private String date;

    // PC + Mobile 합산
    @Column(nullable = false)
    private Long searchCount;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    public KeywordSearchCount(String keyword, String date, Long searchCount, LocalDateTime collectedAt) {
        this.keyword = keyword;
        this.date = date;
        this.searchCount = searchCount;
        this.collectedAt = collectedAt;
    }

    public void update(Long searchCount, LocalDateTime collectedAt) {
        this.searchCount = searchCount;
        this.collectedAt = collectedAt;
    }
}
