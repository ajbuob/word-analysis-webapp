package com.abuob.word.service;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.domain.TextAnalysisReportCount;
import com.abuob.word.dto.WordPairDto;
import com.abuob.word.dto.WordProcessorDto;
import com.abuob.word.processor.WordProcessor;
import com.abuob.word.web.TextProcessingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TextProcessingRequestHandlerImpl implements TextProcessingRequestHandler {

    private static final String ALPHA_AND_WHITESPACE_REGEX = "[^a-zA-Z\\s]";

    private static final String WHITESPACE_REGEX = "\\s+";

    private final WordProcessor wordProcessor;

    private final TextAnalysisReportService textAnalysisReportService;

    public TextProcessingRequestHandlerImpl(WordProcessor wordProcessor, TextAnalysisReportService textAnalysisReportService) {
        this.wordProcessor = wordProcessor;
        this.textAnalysisReportService = textAnalysisReportService;
    }

    @Override
    public void handleTextProcessingRequest(final TextProcessingRequest textProcessingRequest,
                                            final UUID reportKey, final LocalDateTime utcProcessingDateTime) {

        final TextAnalysisReport textAnalysisReport = initializeReport(textProcessingRequest, reportKey, utcProcessingDateTime);
        log.info("Starting text analysis for reportKey: {}", reportKey);

        final StringBuffer processedTextStringBuffer = new StringBuffer();

        final Boolean isExcludeStopWords = textProcessingRequest.isExcludeStopWords();
        final Boolean isGroupStemWords = textProcessingRequest.isGroupStemWords();

        final List<TextAnalysisReportCount> wordCounts = new ArrayList<>();

        final String textToProcess = textProcessingRequest.getText();

        final StringTokenizer stringTokenizer = new StringTokenizer(textToProcess, "\n");

        String line, cleanLine;
        List<String> wordsToProcess;

        WordProcessorDto wordProcessorDto;
        List<WordPairDto> wordPairs;

        final Map<String, Long> wordToCountMap = new ConcurrentHashMap<>();

        while (stringTokenizer.hasMoreTokens()) {
            line = stringTokenizer.nextToken();

            cleanLine = line.replaceAll(ALPHA_AND_WHITESPACE_REGEX, "");

            //Keep track of the lines being processed to save to the database
            processedTextStringBuffer.append(cleanLine + "\n");

            wordsToProcess = tokenizeLine(cleanLine);

            log.info("Processing line for reportKey: {}", reportKey);
            wordProcessorDto = new WordProcessorDto(wordsToProcess, isExcludeStopWords, isGroupStemWords);

            wordPairs = wordProcessor.processWords(wordProcessorDto);

            String word;
            Long count;
            log.info("Updating text analysis pairs for reportKey: {}", reportKey);
            for (WordPairDto wordPairDto : wordPairs) {
                word = wordPairDto.getWord();
                count = wordPairDto.getCount();
                wordToCountMap.put(word, wordToCountMap.getOrDefault(word, 0L) + count);
            }
        }

        log.info("Building text analysis count for reportKey: {}", reportKey);
        TextAnalysisReportCount textAnalysisReportCount;
        for (Map.Entry<String, Long> entry : wordToCountMap.entrySet()) {
            String word = entry.getKey();
            Long count = entry.getValue();
            textAnalysisReportCount = new TextAnalysisReportCount(word, count);
            wordCounts.add(textAnalysisReportCount);
            //Set report in report count to complete bi-directional relationship
            textAnalysisReportCount.setTextAnalysisReport(textAnalysisReport);

        }

        //Set word counts and text before saving the report
        textAnalysisReport.setWordCounts(wordCounts);
        textAnalysisReport.setText(processedTextStringBuffer.toString());

        log.info("Saving text analysis with reportKey: {}", reportKey);
        textAnalysisReportService.saveAnalysisReport(textAnalysisReport);
    }

    private static TextAnalysisReport initializeReport(final TextProcessingRequest textProcessingRequest,
                                                       final UUID reportKey, final LocalDateTime utcProcessingDateTime) {

        final TextAnalysisReport textAnalysisReport = new TextAnalysisReport();
        textAnalysisReport.setReportKey(reportKey);
        textAnalysisReport.setUtcProcessingDateTime(utcProcessingDateTime);

        textAnalysisReport.setGroupStemWords(textProcessingRequest.isGroupStemWords());
        textAnalysisReport.setExcludeStopWords(textProcessingRequest.isExcludeStopWords());

        return textAnalysisReport;
    }

    private static List<String> tokenizeLine(String line) {
        return Arrays.asList(line.split(WHITESPACE_REGEX));
    }
}
