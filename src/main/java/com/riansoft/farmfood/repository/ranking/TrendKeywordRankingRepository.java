package com.riansoft.farmfood.repository.ranking;

import com.riansoft.farmfood.domain.ranking.TrendKeywordRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrendKeywordRankingRepository extends JpaRepository<TrendKeywordRanking, Long> {

    List<TrendKeywordRanking> findTop20ByOrderByRankAsc();
}
