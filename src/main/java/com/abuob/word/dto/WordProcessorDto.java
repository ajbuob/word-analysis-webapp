package com.abuob.word.dto;

import java.util.List;

public class WordProcessorDto {

    private final List<String> wordToProcessList;

    private final boolean isIncludeStopWords;

    private final boolean isGroupStemWords;

    public WordProcessorDto(List<String> wordToProcessList, boolean isIncludeStopWords, boolean isGroupStemWords) {
        this.wordToProcessList = wordToProcessList;
        this.isIncludeStopWords = isIncludeStopWords;
        this.isGroupStemWords = isGroupStemWords;
    }

    public List<String> getWordToProcessList() {
        return wordToProcessList;
    }

    public boolean isIncludeStopWords() {
        return isIncludeStopWords;
    }

    public boolean isGroupStemWords() {
        return isGroupStemWords;
    }
}
