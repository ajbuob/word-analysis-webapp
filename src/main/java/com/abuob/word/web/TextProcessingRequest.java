package com.abuob.word.web;

public class TextProcessingRequest {

    private String text;

    private boolean isExcludeStopWords;

    private boolean isGroupStemWords;

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

    public TextProcessingRequest() {
    }

    public TextProcessingRequest(String text, boolean isExcludeStopWords, boolean isGroupStemWords) {
        this.text = text;
        this.isExcludeStopWords = isExcludeStopWords;
        this.isGroupStemWords = isGroupStemWords;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TextProcessingRequest{");
        sb.append("text='").append(text).append('\'');
        sb.append(", isExcludeStopWords=").append(isExcludeStopWords);
        sb.append(", isGroupStemWords=").append(isGroupStemWords);
        sb.append('}');
        return sb.toString();
    }
}
