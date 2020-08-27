package com.abuob.word.web;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder({"reportId", "utcProcessingDateTime", "excludeStopWords", "groupStemWords", "wordCounts"})
public class TextAnalysisResponse {

    private UUID reportId;

    private LocalDateTime utcProcessingDateTime;

    private boolean isExcludeStopWords;

    private boolean isGroupStemWords;

    private List<TextAnalysisCountResponse> wordCounts;

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public LocalDateTime getUtcProcessingDateTime() {
        return utcProcessingDateTime;
    }

    public void setUtcProcessingDateTime(LocalDateTime utcProcessingDateTime) {
        this.utcProcessingDateTime = utcProcessingDateTime;
    }

    public boolean isExcludeStopWords() {
        return isExcludeStopWords;
    }

    public void setExcludeStopWords(boolean excludeStopWords) {
        isExcludeStopWords = excludeStopWords;
    }

    public boolean isGroupStemWords() {
        return isGroupStemWords;
    }

    public void setGroupStemWords(boolean groupStemWords) {
        isGroupStemWords = groupStemWords;
    }

    public List<TextAnalysisCountResponse> getWordCounts() {
        return wordCounts;
    }

    public void setWordCounts(List<TextAnalysisCountResponse> wordCounts) {
        this.wordCounts = wordCounts;
    }
}
