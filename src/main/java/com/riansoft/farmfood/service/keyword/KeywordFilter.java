package com.riansoft.farmfood.service.keyword;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class KeywordFilter {

    private static final Pattern ONLY_DIGITS = Pattern.compile("^[0-9]+$");

    private final Set<String> stopwords = new HashSet<>();

    @PostConstruct
    public void loadStopwords() {
        ClassPathResource resource = new ClassPathResource("stopwords/food-stopwords.txt");

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isBlank())
                    .forEach(stopwords::add);

        } catch (Exception e) {
            throw new IllegalStateException("불용어 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    public boolean isValid(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }

        if (stopwords.contains(keyword)) {
            return false;
        }

        if (ONLY_DIGITS.matcher(keyword).matches()) {
            return false;
        }

        return true;
    }
}
