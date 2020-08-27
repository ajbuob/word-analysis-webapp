package com.abuob.word.service;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.domain.TextAnalysisReportCount;
import com.abuob.word.repository.TextAnalysisReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TextAnalysisReportServiceImpl implements TextAnalysisReportService {

    private static final Integer MAX_RESULTS_COUNT = 25;

    private final TextAnalysisReportRepository textAnalysisReportRepository;

    @Autowired
    public TextAnalysisReportServiceImpl(TextAnalysisReportRepository textAnalysisReportRepository) {
        this.textAnalysisReportRepository = textAnalysisReportRepository;
    }

    @Override
    public Optional<TextAnalysisReport> saveAnalysisReport(TextAnalysisReport textAnalysisReport) {
        if (textAnalysisReport == null) {
            return Optional.empty();
        }
        return Optional.of(textAnalysisReportRepository.save(textAnalysisReport));
    }

    @Override
    public List<TextAnalysisReport> saveAnalysisReportList(final List<TextAnalysisReport> textAnalysisReportsList) {
        if (CollectionUtils.isEmpty(textAnalysisReportsList)) {
            return Collections.emptyList();
        }
        return textAnalysisReportRepository.saveAll(textAnalysisReportsList);
    }

    @Override
    public Optional<TextAnalysisReport> findTextAnalysisByReportKey(UUID reportKey) {
        if (reportKey == null) {
            return Optional.empty();
        }

        log.info("Looking for report with reportKey: {}", reportKey);
        Optional<TextAnalysisReport> textAnalysisReportOptional = textAnalysisReportRepository.findByReportKey(reportKey);

        if (textAnalysisReportOptional.isPresent()) {
            log.info("Report found with reportKey: {}", reportKey);
            TextAnalysisReport textAnalysisReport = textAnalysisReportOptional.get();

            List<TextAnalysisReportCount> wordCounts = textAnalysisReport.getWordCounts();
            int size = wordCounts.size();

            //Sort in descending order by count so most relevant words are at the beginning
            log.info("Sorting report with reportKey: {}", reportKey);
            wordCounts.sort(Comparator.comparing(TextAnalysisReportCount::getCount).reversed());

            List<TextAnalysisReportCount> wordCountsSorted;
            if (size > MAX_RESULTS_COUNT) {
                log.info("Max count exceeded for report with reportKey: {}", reportKey);
                wordCountsSorted = wordCounts.subList(0, MAX_RESULTS_COUNT);
            } else {
                log.info("Max count not exceeded for report with reportKey: {}", reportKey);
                wordCountsSorted = wordCounts;
            }
            textAnalysisReport.setWordCounts(wordCountsSorted);

            return Optional.of(textAnalysisReport);
        } else {
            log.error("Report NOT found with reportKey: {}", reportKey);
            return Optional.empty();
        }
    }
}
