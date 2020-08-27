package com.abuob.word.dto;

public class WordPairDto {

    private final String word;

    private final Long count;

    public WordPairDto(String word, Long count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordPairDto that = (WordPairDto) o;

        if (!word.equals(that.word)) return false;
        return count.equals(that.count);
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + count.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WordPairDto{");
        sb.append("word='").append(word).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
