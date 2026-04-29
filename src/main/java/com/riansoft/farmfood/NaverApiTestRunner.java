package com.riansoft.farmfood;

import com.riansoft.farmfood.external.naver.NaverSearchClient;
import com.riansoft.farmfood.external.naver.dto.NaverSearchResponse;
import com.riansoft.farmfood.service.search.SearchContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverApiTestRunner implements CommandLineRunner {

    private final SearchContentService searchContentService;

    @Override
    public void run(String... args) throws Exception {
        searchContentService.collectBlogSearchContents("사과");

    }
}
