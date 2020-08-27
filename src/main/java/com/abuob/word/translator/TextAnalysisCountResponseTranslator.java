package com.abuob.word.translator;

import com.abuob.word.domain.TextAnalysisReportCount;
import com.abuob.word.web.TextAnalysisCountResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TextAnalysisCountResponseTranslator {

    public TextAnalysisCountResponse toResponse(final TextAnalysisReportCount textAnalysisReportCount) {

        if (textAnalysisReportCount == null) {
            return null;
        }

        TextAnalysisCountResponse textAnalysisCountResponse = new TextAnalysisCountResponse();
        textAnalysisCountResponse.setWord(textAnalysisReportCount.getWord());
        textAnalysisCountResponse.setCount(textAnalysisReportCount.getCount());
        return textAnalysisCountResponse;
    }


    public List<TextAnalysisCountResponse> toResponseList(final List<TextAnalysisReportCount> textAnalysisReportCountList) {

        if (CollectionUtils.isEmpty(textAnalysisReportCountList)) {
            return null;
        }

        return textAnalysisReportCountList.stream()
                .map(this::toResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
