package com.abuob.word.service;

import com.abuob.word.repository.StopWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;

@Service
public class StopWordServiceImpl implements StopWordService {

    private final StopWordRepository stopWordRepository;

    private Set<String> stopWordSet;

    @Autowired
    public StopWordServiceImpl(StopWordRepository stopWordRepository) {
        this.stopWordRepository = stopWordRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        stopWordSet = stopWordRepository.findStopWords();
    }

    @Override
    public Set<String> findStopWords() {
        return stopWordSet;
    }
}
