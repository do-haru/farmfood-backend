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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtractedKeywordService {

    private final Komoran komoran;
    private final SearchContentRepository searchContentRepository;
    private final ExtractedKeywordRepository extractedKeywordRepository;
    private final KeywordFilter keywordFilter;


    @Transactional
    public void extractKeywords() {
        LocalDateTime extractedDate = LocalDateTime.now();

        extractedKeywordRepository.deleteAll();

        List<SearchContent> contents = searchContentRepository.findAll();

        for (SearchContent content : contents) {
            String text = buildText(content);

            KomoranResult result = komoran.analyze(text);
            List<String> nouns = result.getNouns();

            for (String noun : nouns) {
                if (!keywordFilter.isValid(noun)) {
                    continue;
                }
                saveOrIncreaseKeyword(noun, content, extractedDate);
            }
        }
    }

    private String buildText(SearchContent content) {
        String title = content.getTitle() == null ? "" : content.getTitle();
        String description = content.getDescription() == null ? "" : content.getDescription();

        return title + " " + description;
    }

    private void saveOrIncreaseKeyword(String keyword, SearchContent content, LocalDateTime extractedDate) {
        extractedKeywordRepository
                .findByKeywordAndSourceType(
                        keyword,
                        content.getSourceType()
                )
                .ifPresentOrElse(
                        extractedKeyword -> extractedKeyword.increaseFrequencyAndUpdateDate(extractedDate),
                        () -> extractedKeywordRepository.save(
                              new ExtractedKeyword(
                                      keyword,
                                      content.getSourceType(),
                                      1,
                                      extractedDate
                              )
                        )
                );
    }
}
