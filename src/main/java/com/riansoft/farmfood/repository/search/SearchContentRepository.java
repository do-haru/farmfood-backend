package com.riansoft.farmfood.repository.search;

import com.riansoft.farmfood.domain.search.SearchContent;
import com.riansoft.farmfood.domain.search.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchContentRepository extends JpaRepository<SearchContent, Long> {
    boolean existsBySourceTypeAndLink(SourceType sourceType, String link);

    List<SearchContent> findByCollectedAtAfter(LocalDateTime collectedAt);
}
