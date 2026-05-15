package com.riansoft.farmfood.domain.metric;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class KeywordDailySearchEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private LocalDate searchDate;

    // DataLab 지수 × SA 스케일 팩터로 역산한 추정 일별 검색수
    @Column(nullable = false)
    private Long searchCount;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    public KeywordDailySearchEstimate(String keyword, LocalDate searchDate, Long searchCount, LocalDateTime collectedAt) {
        this.keyword = keyword;
        this.searchDate = searchDate;
        this.searchCount = searchCount;
        this.collectedAt = collectedAt;
    }

    public void update(Long searchCount, LocalDateTime collectedAt) {
        this.searchCount = searchCount;
        this.collectedAt = collectedAt;
    }
}
