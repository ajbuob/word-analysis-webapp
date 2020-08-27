package com.abuob.word.service;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.domain.TextAnalysisReportCount;
import com.abuob.word.repository.TextAnalysisReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

public class TextAnalysisReportServiceTest {

    private TextAnalysisReportService textAnalysisReportService;

    private final TextAnalysisReportRepository textAnalysisReportRepository = mock(TextAnalysisReportRepository.class);

    private TextAnalysisReport textAnalysisReportWithCount;

    private TextAnalysisReportCount textAnalysisReportCount;

    private List<TextAnalysisReportCount> wordCounts;

    private static final UUID TEST_REPORT_KEY = UUID.randomUUID();

    private static final String TEST_TEXT = "ABC XYZ PQR";

    private static final LocalDateTime UTC_NOW = LocalDateTime.now(ZoneOffset.UTC);

    private static final String TEST_WORD = "ABCXYZ";

    private static final Long TEST_COUNT = 21L;

    @BeforeEach
    public void setup() {
        textAnalysisReportService = new TextAnalysisReportServiceImpl(textAnalysisReportRepository);

        textAnalysisReportCount = new TextAnalysisReportCount();
        textAnalysisReportCount.setWord(TEST_WORD);
        textAnalysisReportCount.setCount(TEST_COUNT);

        textAnalysisReportWithCount = new TextAnalysisReport();
        textAnalysisReportWithCount.setReportKey(TEST_REPORT_KEY);
        textAnalysisReportWithCount.setText(TEST_TEXT);
        textAnalysisReportWithCount.setExcludeStopWords(Boolean.TRUE);
        textAnalysisReportWithCount.setGroupStemWords(Boolean.TRUE);
        wordCounts = Collections.singletonList(textAnalysisReportCount);
        textAnalysisReportWithCount.setWordCounts(wordCounts);
        textAnalysisReportWithCount.setUtcProcessingDateTime(UTC_NOW);
    }


    @Test
    public void saveAnalysisReport_null() {
        assertThat(textAnalysisReportService.saveAnalysisReport(null)).isNotPresent();
    }

    @Test
    public void saveAnalysisReport_success() {
        when(textAnalysisReportRepository.save(any(TextAnalysisReport.class))).thenReturn(textAnalysisReportWithCount);
        assertThat(textAnalysisReportService.saveAnalysisReport(textAnalysisReportWithCount)).isNotNull();
    }

    @Test
    public void saveAnalysisReportList_null() {
        assertThat(textAnalysisReportService.saveAnalysisReportList(null)).isEmpty();
    }

    @Test
    public void saveAnalysisReportList_empty() {
        assertThat(textAnalysisReportService.saveAnalysisReportList(Collections.emptyList())).isEmpty();
    }

    @Test
    public void saveAnalysisReportList_success() {
        when(textAnalysisReportRepository.saveAll(anyIterable())).thenReturn(Collections.singletonList(textAnalysisReportWithCount));
        assertThat(textAnalysisReportService.saveAnalysisReportList(Collections.singletonList(textAnalysisReportWithCount))).isNotEmpty();
    }

    @Test
    public void findTextAnalysisByReportKey_null() {
        assertThat(textAnalysisReportService.findTextAnalysisByReportKey(null)).isNotPresent();
    }

    @Test
    public void findTextAnalysisByReportKey_notFound() {
        assertThat(textAnalysisReportService.findTextAnalysisByReportKey(UUID.randomUUID())).isNotPresent();
    }

    @Test
    public void findTextAnalysisByReportKey_success_sorted_noTruncate() {

        final TextAnalysisReport textAnalysisReport = new TextAnalysisReport();
        final List<TextAnalysisReportCount> wordCountsNoTruncateList = new ArrayList<>();

        TextAnalysisReportCount textAnalysisReportCountCurrent;

        for (int i = 1; i <= 10; i++) {
            textAnalysisReportCountCurrent = new TextAnalysisReportCount();
            textAnalysisReportCountCurrent.setCount(i * 10L);
            textAnalysisReportCountCurrent.setWord("ABC");
            wordCountsNoTruncateList.add(textAnalysisReportCountCurrent);
        }

        textAnalysisReport.setReportKey(TEST_REPORT_KEY);
        textAnalysisReport.setText(TEST_TEXT);
        textAnalysisReport.setExcludeStopWords(Boolean.TRUE);
        textAnalysisReport.setGroupStemWords(Boolean.TRUE);
        textAnalysisReport.setUtcProcessingDateTime(UTC_NOW);
        textAnalysisReport.setWordCounts(wordCountsNoTruncateList);

        when(textAnalysisReportRepository.findByReportKey(any(UUID.class))).thenReturn(Optional.of(textAnalysisReport));

        Optional<TextAnalysisReport> resultOptional = textAnalysisReportService.findTextAnalysisByReportKey(UUID.randomUUID());
        assertThat(resultOptional).isPresent();

        final TextAnalysisReport result = resultOptional.get();

        List<TextAnalysisReportCount> wordCountResult = result.getWordCounts();
        assertThat(wordCountResult).isNotNull();
        assertThat(wordCountResult).isNotEmpty();
        assertThat(wordCountResult).hasSize(10);
        assertThat(wordCountResult.get(0).getCount()).isEqualTo(100L);
    }

    @Test
    public void findTextAnalysisByReportKey_success_sorted_truncateList() {

        final TextAnalysisReport textAnalysisReport = new TextAnalysisReport();
        final List<TextAnalysisReportCount> wordCountsNoTruncateList = new ArrayList<>();

        TextAnalysisReportCount textAnalysisReportCountCurrent;

        for (int i = 1; i <= 100; i++) {
            textAnalysisReportCountCurrent = new TextAnalysisReportCount();
            textAnalysisReportCountCurrent.setCount(i * 10L);
            textAnalysisReportCountCurrent.setWord("ABC");
            wordCountsNoTruncateList.add(textAnalysisReportCountCurrent);
        }

        textAnalysisReport.setReportKey(TEST_REPORT_KEY);
        textAnalysisReport.setText(TEST_TEXT);
        textAnalysisReport.setExcludeStopWords(Boolean.TRUE);
        textAnalysisReport.setGroupStemWords(Boolean.TRUE);
        textAnalysisReport.setUtcProcessingDateTime(UTC_NOW);
        textAnalysisReport.setWordCounts(wordCountsNoTruncateList);

        when(textAnalysisReportRepository.findByReportKey(any(UUID.class))).thenReturn(Optional.of(textAnalysisReport));

        Optional<TextAnalysisReport> resultOptional = textAnalysisReportService.findTextAnalysisByReportKey(UUID.randomUUID());
        assertThat(resultOptional).isPresent();

        final TextAnalysisReport result = resultOptional.get();

        List<TextAnalysisReportCount> wordCountResult = result.getWordCounts();
        assertThat(wordCountResult).isNotNull();
        assertThat(wordCountResult).isNotEmpty();
        assertThat(wordCountResult).hasSize(25);
        assertThat(wordCountResult.get(0).getCount()).isEqualTo(1000L);
    }


}
