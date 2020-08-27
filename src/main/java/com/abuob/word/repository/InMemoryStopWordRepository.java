package com.abuob.word.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemoryStopWordRepository implements StopWordRepository {

    private Set<String> stopWordSet;

    @Value("classpath:data/stopwords.txt")
    private Resource resource;

    public InMemoryStopWordRepository() {
        stopWordSet = new HashSet<>();
    }

    @PostConstruct
    public void init() throws IOException {
        loadStopWords();
    }

    private void loadStopWords() throws IOException {
        final String filePath = resource.getFile().getAbsolutePath();
        List<String> wordsList = Files.readAllLines(Paths.get(filePath));
        stopWordSet = wordsList.stream().collect(Collectors.toSet());

    }

    @Override
    public Set<String> findStopWords() {
        return stopWordSet;
    }
}
