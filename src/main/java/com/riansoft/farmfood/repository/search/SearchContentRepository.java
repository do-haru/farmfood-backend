package com.riansoft.farmfood.repository.search;

import com.riansoft.farmfood.domain.search.SearchContent;
import com.riansoft.farmfood.domain.search.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchContentRepository extends JpaRepository<SearchContent, Long> {
    boolean existsBySourceTypeAndLink(SourceType sourceType, String link);
}
