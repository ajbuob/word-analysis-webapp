package com.abuob.word.translator;

import com.abuob.word.domain.TextAnalysisReportCount;
import com.abuob.word.web.TextAnalysisCountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TextAnalysisCountResponseTranslatorTest {

    private TextAnalysisCountResponseTranslator textAnalysisCountResponseTranslator;

    private TextAnalysisReportCount textAnalysisReportCount;

    private static final String TEST_WORD = "ABCXYZ";

    private static final Long TEST_COUNT = 21L;


    @BeforeEach
    public void setup() {

        textAnalysisCountResponseTranslator = new TextAnalysisCountResponseTranslator();

        textAnalysisReportCount = new TextAnalysisReportCount();
        textAnalysisReportCount.setWord(TEST_WORD);
        textAnalysisReportCount.setCount(TEST_COUNT);

    }

    @Test
    public void toResponse_Null() {
        assertThat(textAnalysisCountResponseTranslator.toResponse(null)).isNull();
    }


    @Test
    public void toResponse_Success() {

        final TextAnalysisCountResponse response =
                textAnalysisCountResponseTranslator.toResponse(textAnalysisReportCount);

        assertThat(response).isNotNull();
        assertThat(response.getWord()).isEqualTo(TEST_WORD);
        assertThat(response.getCount()).isEqualTo(TEST_COUNT);

    }


    @Test
    public void toResponseList_Null() {
        assertThat(textAnalysisCountResponseTranslator.toResponseList(null)).isNull();
    }


    @Test
    public void toResponseList_Empty() {
        assertThat(textAnalysisCountResponseTranslator.toResponseList(Collections.emptyList())).isNull();
    }


    @Test
    public void toResponseList_Success() {
        final List<TextAnalysisCountResponse> responseList =
                textAnalysisCountResponseTranslator.toResponseList(Collections.singletonList(textAnalysisReportCount));

        assertThat(responseList).isNotNull();
        assertThat(responseList).isNotEmpty();
        assertThat(responseList).hasSize(1);

        final TextAnalysisCountResponse response = responseList.get(0);

        assertThat(response).isNotNull();
        assertThat(response.getWord()).isEqualTo(TEST_WORD);
        assertThat(response.getCount()).isEqualTo(TEST_COUNT);
    }

}
