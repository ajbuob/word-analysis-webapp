package com.abuob.word.web;

import java.time.LocalDateTime;
import java.util.UUID;

public class TextProcessingResponse {

    private UUID reportId;

    private LocalDateTime utcProcessingDateTime;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TextProcessingResponse{");
        sb.append("reportId=").append(reportId);
        sb.append(", utcProcessingDateTime=").append(utcProcessingDateTime);
        sb.append('}');
        return sb.toString();
    }
}
