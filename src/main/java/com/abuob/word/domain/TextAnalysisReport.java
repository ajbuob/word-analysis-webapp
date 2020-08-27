package com.abuob.word.domain;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "text_analysis_report",
        indexes = {@Index(name = "report_key_idx", columnList = "report_key")})
public class TextAnalysisReport {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "report_key", nullable = false, updatable = false)
    private UUID reportKey;

    @Column(name = "text", nullable = false, updatable = false)
    private String text;

    @Column(name = "exclude_stop_words", nullable = false, updatable = false)
    private boolean isExcludeStopWords;

    @Column(name = "group_stem_words", nullable = false, updatable = false)
    private boolean isGroupStemWords;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "textAnalysisReport",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextAnalysisReportCount> wordCounts;

    @Column(name = "processing_date_time", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime utcProcessingDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getReportKey() {
        return reportKey;
    }

    public void setReportKey(UUID reportKey) {
        this.reportKey = reportKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<TextAnalysisReportCount> getWordCounts() {
        return wordCounts;
    }

    public void setWordCounts(List<TextAnalysisReportCount> wordCounts) {
        this.wordCounts = wordCounts;
    }

    public LocalDateTime getUtcProcessingDateTime() {
        return utcProcessingDateTime;
    }

    public void setUtcProcessingDateTime(LocalDateTime utcProcessingDateTime) {
        this.utcProcessingDateTime = utcProcessingDateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TextAnalysisReport{");
        sb.append("id=").append(id);
        sb.append(", reportKey=").append(reportKey);
        sb.append(", text='").append(text).append('\'');
        sb.append(", isExcludeStopWords=").append(isExcludeStopWords);
        sb.append(", isGroupStemWords=").append(isGroupStemWords);
        sb.append(", utcProcessingDateTime=").append(utcProcessingDateTime);
        sb.append('}');
        return sb.toString();
    }
}
