package com.riansoft.farmfood.repository.keyword;

import com.riansoft.farmfood.domain.keyword.ExtractedKeyword;
import com.riansoft.farmfood.domain.search.SourceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExtractedKeywordRepository extends JpaRepository<ExtractedKeyword, Long> {

    Optional<ExtractedKeyword> findByKeywordAndSourceType(
            String keyword,
            SourceType sourceType
    );

    Optional<ExtractedKeyword> findTopByOrderByExtractedDateDesc();

    List<ExtractedKeyword> findTop20ByOrderByFrequencyDesc();

    @Query("""
        select e.keyword as keyword,
               sum(e.frequency) as totalFrequency
        from ExtractedKeyword e
        group by e.keyword
        order by sum(e.frequency) desc
        """)
    List<KeywordFrequencySummary> findKeywordFrequencySummaries(Pageable pageable);
}
