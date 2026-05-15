package com.riansoft.farmfood.repository.metric;

import com.riansoft.farmfood.domain.metric.KeywordSearchCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordSearchCountRepository extends JpaRepository<KeywordSearchCount, Long> {

    Optional<KeywordSearchCount> findByKeywordAndDate(String keyword, String date);
}
