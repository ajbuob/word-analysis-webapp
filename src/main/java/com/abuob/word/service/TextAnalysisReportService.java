package com.abuob.word.service;

import com.abuob.word.domain.TextAnalysisReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TextAnalysisReportService {

    Optional<TextAnalysisReport> saveAnalysisReport(TextAnalysisReport textAnalysisReport);

    List<TextAnalysisReport> saveAnalysisReportList(List<TextAnalysisReport> textAnalysisReportList);

    Optional<TextAnalysisReport> findTextAnalysisByReportKey(UUID reportKey);
}
