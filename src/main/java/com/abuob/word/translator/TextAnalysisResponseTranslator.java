package com.abuob.word.translator;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.web.TextAnalysisResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextAnalysisResponseTranslator {

    private final TextAnalysisCountResponseTranslator textAnalysisCountResponseTranslator;

    @Autowired
    public TextAnalysisResponseTranslator(TextAnalysisCountResponseTranslator textAnalysisCountResponseTranslator) {
        this.textAnalysisCountResponseTranslator = textAnalysisCountResponseTranslator;
    }

    public TextAnalysisResponse toResponse(final TextAnalysisReport textAnalysisReport) {
        if (textAnalysisReport == null) {
            return null;
        }

        TextAnalysisResponse textAnalysisResponse = new TextAnalysisResponse();
        textAnalysisResponse.setReportId(textAnalysisReport.getReportKey());
        textAnalysisResponse.setUtcProcessingDateTime(textAnalysisReport.getUtcProcessingDateTime());
        textAnalysisResponse.setExcludeStopWords(textAnalysisReport.isExcludeStopWords());
        textAnalysisResponse.setGroupStemWords(textAnalysisReport.isGroupStemWords());

        if (CollectionUtils.isNotEmpty(textAnalysisReport.getWordCounts())) {
            textAnalysisResponse.setWordCounts(textAnalysisCountResponseTranslator.toResponseList(textAnalysisReport.getWordCounts()));
        }
        return textAnalysisResponse;
    }
}
