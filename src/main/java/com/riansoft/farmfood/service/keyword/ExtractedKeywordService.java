package com.riansoft.farmfood.service.keyword;

import com.riansoft.farmfood.domain.keyword.ExtractedKeyword;
import com.riansoft.farmfood.domain.search.SearchContent;
import com.riansoft.farmfood.repository.keyword.ExtractedKeywordRepository;
import com.riansoft.farmfood.repository.search.SearchContentRepository;
import jakarta.transaction.Transactional;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtractedKeywordService {

    private final Komoran komoran;
    private final SearchContentRepository searchContentRepository;
    private final ExtractedKeywordRepository extractedKeywordRepository;


    @Transactional
    public void extractKeywords() {
        List<SearchContent> contents = searchContentRepository.findAll();

        for (SearchContent content : contents) {
            String text = buildText(content);

            KomoranResult result = komoran.analyze(text);
            List<String> nouns = result.getNouns();

            for (String noun : nouns) {
                saveOrIncreaseKeyword(noun, content);
            }
        }
    }

    private String buildText(SearchContent content) {
        String title = content.getTitle() == null ? "" : content.getTitle();
        String description = content.getDescription() == null ? "" : content.getDescription();

        return title + " " + description;
    }

    private void saveOrIncreaseKeyword(String keyword, SearchContent content) {
        LocalDate collectedDate = content.getCollectedAt().toLocalDate();

        extractedKeywordRepository
                .findByKeywordAndSourceTypeAndCollectedDate(
                        keyword,
                        content.getSourceType(),
                        collectedDate
                )
                .ifPresentOrElse(
                        ExtractedKeyword::increaseFrequency,
                        () -> extractedKeywordRepository.save(
                              new ExtractedKeyword(
                                      keyword,
                                      content.getSourceType(),
                                      1,
                                      collectedDate
                              )
                        )
                );
    }
}
