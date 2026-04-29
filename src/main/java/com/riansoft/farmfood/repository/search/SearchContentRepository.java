package com.riansoft.farmfood.repository.search;

import com.riansoft.farmfood.domain.search.SearchContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchContentRepository extends JpaRepository<SearchContent, Long> {
}
