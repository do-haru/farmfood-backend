package com.riansoft.farmfood.repository.keyword;

import com.riansoft.farmfood.domain.keyword.ExtractedKeyword;
import com.riansoft.farmfood.domain.search.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ExtractedKeywordRepository extends JpaRepository<ExtractedKeyword, Long> {

    Optional<ExtractedKeyword> findByKeywordAndSourceTypeAndExtractedDate(
            String keyword,
            SourceType sourceType,
            LocalDateTime extractedDate
    );
}
