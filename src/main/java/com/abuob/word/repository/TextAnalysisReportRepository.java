package com.abuob.word.repository;

import com.abuob.word.domain.TextAnalysisReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TextAnalysisReportRepository extends JpaRepository<TextAnalysisReport, Long> {

    Optional<TextAnalysisReport> findByReportKey(UUID reportKey);
}
