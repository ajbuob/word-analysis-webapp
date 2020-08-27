package com.abuob.word.repository;

import com.abuob.word.domain.TextAnalysisReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TextAnalysisReportRepositoryTest {

    @Autowired
    private TextAnalysisReportRepository textAnalysisReportRepository;

    private TextAnalysisReport textAnalysisReport;

    private static final UUID TEST_REPORT_KEY = UUID.randomUUID();

    private static final String TEST_TEXT = "BGE SDF YGE";

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
    public void testFindByReportKey() {

        textAnalysisReportRepository.save(textAnalysisReport);

        Optional<TextAnalysisReport> resultOptional = textAnalysisReportRepository.findByReportKey(TEST_REPORT_KEY);

        assertThat(resultOptional).isPresent();
    }
}
