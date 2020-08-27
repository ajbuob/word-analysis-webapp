package com.abuob.word.translator;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.web.TextAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class TextAnalysisResponseTranslatorTest {

    @Mock
    private TextAnalysisCountResponseTranslator textAnalysisCountResponseTranslatorMock;

    @InjectMocks
    private TextAnalysisResponseTranslator textAnalysisResponseTranslator;

    private TextAnalysisReport textAnalysisReport;

    private static final UUID TEST_REPORT_KEY = UUID.randomUUID();

    private static final String TEST_TEXT = "ABC XYZ PQR";

    private static final LocalDateTime UTC_NOW = LocalDateTime.now(ZoneOffset.UTC);

    @BeforeEach
    public void setup() {

        textAnalysisReport = new TextAnalysisReport();
        textAnalysisReport.setReportKey(TEST_REPORT_KEY);
        textAnalysisReport.setText(TEST_TEXT);
        textAnalysisReport.setExcludeStopWords(Boolean.TRUE);
        textAnalysisReport.setGroupStemWords(Boolean.TRUE);
        textAnalysisReport.setWordCounts(Collections.emptyList());
        textAnalysisReport.setUtcProcessingDateTime(UTC_NOW);
    }

    @Test
    public void toResponse_Null() {
        assertThat(textAnalysisResponseTranslator.toResponse(null)).isNull();
    }

    @Test
    public void toResponse_Success() {

        final TextAnalysisResponse response =
                textAnalysisResponseTranslator.toResponse(textAnalysisReport);

        assertThat(response).isNotNull();
        assertThat(response.getReportId()).isEqualTo(TEST_REPORT_KEY);
        assertThat(response.getUtcProcessingDateTime()).isEqualTo(UTC_NOW);
        assertThat(response.isExcludeStopWords()).isTrue();
        assertThat(response.isGroupStemWords()).isTrue();
        assertThat(response.getWordCounts()).isNull();
    }
}
