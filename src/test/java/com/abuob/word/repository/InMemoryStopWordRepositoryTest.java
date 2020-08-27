package com.abuob.word.repository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class InMemoryStopWordRepositoryTest {

    @Autowired
    private StopWordRepository stopWordRepository;

    @Test
    public void testStopWordsSet() {
        Set<String> stopWordsSet = stopWordRepository.findStopWords();

        assertThat(stopWordsSet)
                .isNotNull()
                .isInstanceOf(HashSet.class)
                .isNotEmpty()
                .hasSize(124);
    }
}
