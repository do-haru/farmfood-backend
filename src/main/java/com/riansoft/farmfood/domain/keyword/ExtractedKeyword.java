package com.riansoft.farmfood.domain.keyword;

import com.riansoft.farmfood.domain.search.SourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ExtractedKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType sourceType;

    @Column(nullable = false)
    private Integer frequency;

    @Column(nullable = false)
    private LocalDateTime extractedDate;

    public ExtractedKeyword(String keyword,
                            SourceType sourceType,
                            Integer frequency,
                            LocalDateTime extractedDate) {
        this.keyword = keyword;
        this.sourceType = sourceType;
        this.frequency = frequency;
        this.extractedDate = extractedDate;
    }

    public void increaseFrequencyAndUpdateDate(LocalDateTime extractedDate) {
        this.frequency++;
        this.extractedDate = extractedDate;
    }
}
