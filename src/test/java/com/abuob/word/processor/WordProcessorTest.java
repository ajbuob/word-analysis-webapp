package com.abuob.word.processor;

import com.abuob.word.dto.WordPairDto;
import com.abuob.word.dto.WordProcessorDto;
import com.abuob.word.service.StopWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordProcessorTest {

    private static final Set<String> STOP_WORDS = Set.of("AAA", "JZ");

    private final StopWordService stopWordRepositoryMock = mock(StopWordService.class);

    private WordProcessor wordProcessor;

    @BeforeEach
    public void setup() {
        when(stopWordRepositoryMock.findStopWords()).thenReturn(STOP_WORDS);
        wordProcessor = new WordProcessor(stopWordRepositoryMock);
    }

    @Test
    public void processWords_null() {
        List<WordPairDto> resultList = wordProcessor.processWords(null);

        assertThat(resultList).isNotNull();
        assertThat(resultList).isEmpty();
        assertThat(resultList).hasSize(0);
    }

    @Test
    public void processWords_nullWords() {
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(null, false, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isEmpty();
        assertThat(resultList).hasSize(0);
    }

    @Test
    public void processWords_noWords() {
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(Collections.emptyList(), false, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isEmpty();
        assertThat(resultList).hasSize(0);
    }


    @Test
    public void processWords_ignoreStopWords() {
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(STOP_WORDS.stream().collect(Collectors.toList()), false, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isEmpty();
        assertThat(resultList).hasSize(0);
    }

    @Test
    public void processWords_noIgnoreStopWordsNoProcessing() {
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(STOP_WORDS.stream().collect(Collectors.toList()), true, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList).hasSize(2);

        Iterable<WordPairDto> wordPairDtoIterable = List.of(new WordPairDto("AAA", 1L), new WordPairDto("JZ", 1L));
        assertThat(resultList).containsExactlyInAnyOrderElementsOf(wordPairDtoIterable);
    }

    @Test
    public void processWords_ignoreStopWordsNoProcessing() {
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(List.of("TVS", "VFQ"), true, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList).hasSize(2);

        Iterable<WordPairDto> wordPairDtoIterable = List.of(new WordPairDto("TVS", 1L), new WordPairDto("VFQ", 1L));
        assertThat(resultList).containsExactlyInAnyOrderElementsOf(wordPairDtoIterable);
    }

    @Test
    public void processWords_noSuffixAdd_noGrouping() {
        //LZ -> "" (isGroupStemWords = false will put all stems in different buckets)
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(List.of("JLZ"),
                false, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList).hasSize(1);

        Iterable<WordPairDto> wordPairDtoIterable = List.of(new WordPairDto("JLZ", 1L));
        assertThat(resultList).containsExactlyInAnyOrderElementsOf(wordPairDtoIterable);
    }

    @Test
    public void processWords_noSuffixAdd_withGrouping() {
        //LZ -> "" (isGroupStemWords = true will put all stems in same bucket)
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(List.of("JLZ"),
                false, true));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList).hasSize(1);

        Iterable<WordPairDto> wordPairDtoIterable = List.of(new WordPairDto("J", 1L));
        assertThat(resultList).containsExactlyInAnyOrderElementsOf(wordPairDtoIterable);
    }

    @Test
    public void processWords_suffixAdd_noGrouping() {
        //PZL -> "AZ" (isGroupStemWords = false will put all stems in different buckets)
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(List.of("JQPZL"),
                false, false));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList).hasSize(1);

        Iterable<WordPairDto> wordPairDtoIterable = List.of(new WordPairDto("JQAZ", 1L));
        assertThat(resultList).containsExactlyInAnyOrderElementsOf(wordPairDtoIterable);
    }

    @Test
    public void processWords_SuffixAdd_withGrouping() {
        //PZL -> "AZ" (isGroupStemWords = true will put all stems in same bucket)
        List<WordPairDto> resultList = wordProcessor.processWords(new WordProcessorDto(List.of("JQPZL"),
                true, true));

        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList).hasSize(1);

        Iterable<WordPairDto> wordPairDtoIterable = List.of(new WordPairDto("JQ", 1L));
        assertThat(resultList).containsExactlyInAnyOrderElementsOf(wordPairDtoIterable);
    }
}
