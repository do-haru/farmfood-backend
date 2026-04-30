package com.riansoft.farmfood.domain.search;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SearchContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType sourceType;

    @Column(nullable = false)
    private String searchKeyword;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String link;

    private LocalDate publishedAt;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    public SearchContent(SourceType sourceType,
                         String searchKeyword, String title,
                         String description,
                         String link,
                         LocalDate publishedAt,
                         LocalDateTime collectedAt) {
        this.sourceType = sourceType;
        this.searchKeyword = searchKeyword;
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedAt = publishedAt;
        this.collectedAt = collectedAt;
    }
}
