package com.riansoft.farmfood.controller.admin;

import com.riansoft.farmfood.service.keyword.ExtractedKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/keywords")
@RequiredArgsConstructor
public class AdminKeywordController {

    private final ExtractedKeywordService extractedKeywordService;

    @PostMapping("/extract")
    public String extractKeywords() {
        extractedKeywordService.extractKeywords();

        return "키워드 추출 완료";
    }
}
