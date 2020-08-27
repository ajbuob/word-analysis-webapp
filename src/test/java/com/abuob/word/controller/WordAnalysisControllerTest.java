package com.abuob.word.controller;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.domain.TextAnalysisReportCount;
import com.abuob.word.service.TextAnalysisReportService;
import com.abuob.word.service.TextProcessingRequestHandler;
import com.abuob.word.translator.TextAnalysisResponseTranslator;
import com.abuob.word.web.TextAnalysisCountResponse;
import com.abuob.word.web.TextAnalysisResponse;
import com.abuob.word.web.TextProcessingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WordAnalysisControllerTest {

    @MockBean
    private TextProcessingRequestHandler textProcessingRequestHandler;

    @MockBean
    private TextAnalysisReportService textAnalysisReportService;

    @MockBean
    private TextAnalysisResponseTranslator textAnalysisResponseTranslator;

    @Autowired
    private MockMvc mockMvc;

    private static final UUID TEST_REPORT_KEY = UUID.randomUUID();

    private static final String TEST_TEXT = "ABC XYZ PQR";

    private static final LocalDateTime UTC_NOW = LocalDateTime.now(ZoneOffset.UTC);

    private static final String TEST_WORD = "ABCXYZ";

    private static final Long TEST_COUNT = 21L;

    private TextAnalysisReport textAnalysisReport, textAnalysisReportWithCount;

    private TextAnalysisReportCount textAnalysisReportCount;

    private List<TextAnalysisReportCount> wordCounts;

    private TextAnalysisResponse textAnalysisResponse, textAnalysisResponseWithCount;

    private TextAnalysisCountResponse textAnalysisCountResponse;

    private List<TextAnalysisCountResponse> wordCountsResponse;


    @BeforeEach
    public void setup() {
        //DOMAIN - NO COUNT
        textAnalysisReport = new TextAnalysisReport();
        textAnalysisReport.setReportKey(TEST_REPORT_KEY);
        textAnalysisReport.setText(TEST_TEXT);
        textAnalysisReport.setExcludeStopWords(Boolean.TRUE);
        textAnalysisReport.setGroupStemWords(Boolean.TRUE);
        textAnalysisReport.setWordCounts(Collections.emptyList());
        textAnalysisReport.setUtcProcessingDateTime(UTC_NOW);

        //DOMAIN - COUNT
        textAnalysisReportCount = new TextAnalysisReportCount();
        textAnalysisReportCount.setWord(TEST_WORD);
        textAnalysisReportCount.setCount(TEST_COUNT);

        //DOMAIN - WITH COUNT
        textAnalysisReportWithCount = new TextAnalysisReport();
        textAnalysisReportWithCount.setReportKey(TEST_REPORT_KEY);
        textAnalysisReportWithCount.setText(TEST_TEXT);
        textAnalysisReportWithCount.setExcludeStopWords(Boolean.TRUE);
        textAnalysisReportWithCount.setGroupStemWords(Boolean.TRUE);
        wordCounts = Collections.singletonList(textAnalysisReportCount);
        textAnalysisReportWithCount.setWordCounts(wordCounts);
        textAnalysisReportWithCount.setUtcProcessingDateTime(UTC_NOW);

        //WEB - NO COUNT
        textAnalysisResponse = new TextAnalysisResponse();
        textAnalysisResponse.setReportId(TEST_REPORT_KEY);
        textAnalysisResponse.setExcludeStopWords(Boolean.TRUE);
        textAnalysisResponse.setGroupStemWords(Boolean.TRUE);
        textAnalysisResponse.setWordCounts(wordCountsResponse);
        textAnalysisResponse.setWordCounts(Collections.emptyList());
        textAnalysisResponse.setUtcProcessingDateTime(UTC_NOW);

        //WEB - COUNT
        textAnalysisCountResponse = new TextAnalysisCountResponse();
        textAnalysisCountResponse.setWord(TEST_WORD);
        textAnalysisCountResponse.setCount(TEST_COUNT);

        //WEB  - WITH COUNT
        textAnalysisResponseWithCount = new TextAnalysisResponse();
        textAnalysisResponseWithCount.setReportId(TEST_REPORT_KEY);
        textAnalysisResponseWithCount.setExcludeStopWords(Boolean.TRUE);
        textAnalysisResponseWithCount.setGroupStemWords(Boolean.TRUE);
        wordCountsResponse = Collections.singletonList(textAnalysisCountResponse);
        textAnalysisResponseWithCount.setWordCounts(wordCountsResponse);
        textAnalysisResponseWithCount.setUtcProcessingDateTime(UTC_NOW);
    }


    @Test
    public void processInputText_success() throws Exception {

        final String inputText = "ABC DEF\\n PQR XYZ";

        mockMvc.perform(post("/word-analysis/report")
                .content(buildJsonString(new TextProcessingRequest(inputText, true, true)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportId").isNotEmpty())
                .andExpect(jsonPath("$.utcProcessingDateTime").isNotEmpty());
    }

    @Test
    public void findTextAnalysisById_ReportNotFound() throws Exception {

        final UUID reportId = UUID.randomUUID();
        mockMvc.perform(get("/word-analysis/report/" + reportId).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findTextAnalysisById_ReportFound() throws Exception {

        when(textAnalysisReportService.findTextAnalysisByReportKey(any(UUID.class))).thenReturn(Optional.of(textAnalysisReport));
        when(textAnalysisResponseTranslator.toResponse(any(TextAnalysisReport.class))).thenReturn(textAnalysisResponse);

        mockMvc.perform(get("/word-analysis/report/" + TEST_REPORT_KEY).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.reportId").value(TEST_REPORT_KEY.toString()))
                .andExpect(jsonPath("$.wordCounts").isEmpty())
                .andExpect(jsonPath("$.utcProcessingDateTime").value(UTC_NOW.toString()))
                .andExpect(jsonPath("$.excludeStopWords").value(Boolean.TRUE))
                .andExpect(jsonPath("$.groupStemWords").value(Boolean.TRUE));
    }

    @Test
    public void findTextAnalysisById_ReportFoundWithCount() throws Exception {

        when(textAnalysisReportService.findTextAnalysisByReportKey(any(UUID.class))).thenReturn(Optional.of(textAnalysisReportWithCount));
        when(textAnalysisResponseTranslator.toResponse(any(TextAnalysisReport.class))).thenReturn(textAnalysisResponseWithCount);

        mockMvc.perform(get("/word-analysis/report/" + TEST_REPORT_KEY).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.reportId").value(TEST_REPORT_KEY.toString()))
                .andExpect(jsonPath("$.wordCounts").isNotEmpty())
                .andExpect(jsonPath("$.wordCounts").isArray())
                .andExpect(jsonPath("$.wordCounts[0].word").value(TEST_WORD))
                .andExpect(jsonPath("$.wordCounts[0].count").value(TEST_COUNT))
                .andExpect(jsonPath("$.utcProcessingDateTime").value(UTC_NOW.toString()))
                .andExpect(jsonPath("$.excludeStopWords").value(Boolean.TRUE))
                .andExpect(jsonPath("$.groupStemWords").value(Boolean.TRUE));
    }

    public static String buildJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
