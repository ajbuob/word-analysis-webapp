package com.abuob.word.controller;

import com.abuob.word.domain.TextAnalysisReport;
import com.abuob.word.service.TextAnalysisReportService;
import com.abuob.word.service.TextProcessingRequestHandler;
import com.abuob.word.translator.TextAnalysisResponseTranslator;
import com.abuob.word.web.TextAnalysisResponse;
import com.abuob.word.web.TextProcessingRequest;
import com.abuob.word.web.TextProcessingResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("word-analysis/report")
public class WordAnalysisController {

    private final TextProcessingRequestHandler textProcessingRequestHandler;

    private final TextAnalysisReportService textAnalysisReportService;

    private final TextAnalysisResponseTranslator textAnalysisResponseTranslator;

    @PostMapping
    public ResponseEntity<TextProcessingResponse> processInputText(@RequestBody TextProcessingRequest textProcessingRequest) {

        LocalDateTime utcNow = LocalDateTime.now(ZoneOffset.UTC);
        UUID reportId = UUID.randomUUID();

        TextProcessingResponse textProcessingResponse = new TextProcessingResponse();
        textProcessingResponse.setReportId(reportId);
        textProcessingResponse.setUtcProcessingDateTime(utcNow);

        //Process async and return reportId in response so caller can use it
        //to query for report with GET endpoint
        CompletableFuture.runAsync(() -> {
            log.info("Processing text with reportId: {}", reportId);
            textProcessingRequestHandler.handleTextProcessingRequest(textProcessingRequest, reportId, utcNow);
        });

        log.info("Returning response: {}", textProcessingResponse);
        return new ResponseEntity<>(textProcessingResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextAnalysisResponse> findTextAnalysisById(@PathVariable final UUID reportId) {

        log.info("Searching for report with reportId: {}", reportId);

        Optional<TextAnalysisReport> textAnalysisReportOptional = textAnalysisReportService.findTextAnalysisByReportKey(reportId);

        if (!textAnalysisReportOptional.isPresent()) {
            log.error("No report found for reportId: {}", reportId);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        log.info("Report found for reportId: {}", reportId);
        TextAnalysisResponse textAnalysisResponse = textAnalysisResponseTranslator.toResponse(textAnalysisReportOptional.get());

        log.info("Returning success response for reportId: {}", reportId);
        return new ResponseEntity<>(textAnalysisResponse, HttpStatus.OK);
    }
}
