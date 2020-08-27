package com.abuob.word.domain;

import javax.persistence.*;

@Entity
@Table(name = "text_analysis_report_count")
public class TextAnalysisReportCount {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "word", nullable = false, updatable = false)
    private String word;

    @Column(name = "count", nullable = false, updatable = false)
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TEXT_ANALYSIS_REPORT_ID"))
    TextAnalysisReport textAnalysisReport;

    public TextAnalysisReportCount() {
    }

    public TextAnalysisReportCount(String word, Long count) {
        this.word = word;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public TextAnalysisReport getTextAnalysisReport() {
        return textAnalysisReport;
    }

    public void setTextAnalysisReport(TextAnalysisReport textAnalysisReport) {
        this.textAnalysisReport = textAnalysisReport;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TextAnalysisReportCount{");
        sb.append("id=").append(id);
        sb.append(", word='").append(word).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
