package com.abuob.word.service;

import com.abuob.word.web.TextProcessingRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TextProcessingRequestHandler {

    void handleTextProcessingRequest(final TextProcessingRequest textProcessingRequest,
                                     final UUID reportKey,
                                     final LocalDateTime utcProcessingDateTime);
}
