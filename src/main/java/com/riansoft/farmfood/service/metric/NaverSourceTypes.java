package com.riansoft.farmfood.service.metric;

import com.riansoft.farmfood.domain.search.SourceType;

import java.util.List;

public class NaverSourceTypes {

    public static final List<SourceType> NAVER_SOURCE_TYPES = List.of(
            SourceType.BLOG, SourceType.NEWS, SourceType.CAFE, SourceType.SHOPPING
    );

    private NaverSourceTypes() {}
}
