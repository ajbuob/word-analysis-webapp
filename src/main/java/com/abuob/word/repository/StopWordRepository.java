package com.abuob.word.repository;

import java.util.Set;

public interface StopWordRepository {

    Set<String> findStopWords();
}
