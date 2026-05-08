package com.riansoft.farmfood.service.search;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class FoodSeedKeywordProvider {

    public List<String> getSeedKeywords() {
        ClassPathResource resource = new ClassPathResource("seedkeywords/food-seed-keywords.txt");

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isBlank())
                    .toList();

        } catch (Exception e) {
            throw new IllegalStateException("시드 키워드 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }
}
