package com.abuob.word.processor;

import com.abuob.word.dto.WordPairDto;
import com.abuob.word.dto.WordProcessorDto;
import com.abuob.word.service.StopWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class WordProcessor {

    private final StopWordService stopWordService;

    private final Set<String> stopWordSet;

    private static Map<String, String> suffixToRootSuffixMap;

    @Autowired
    public WordProcessor(StopWordService stopWordService) {
        this.stopWordService = stopWordService;

        stopWordSet = stopWordService.findStopWords();

        //Preserves insertion order(most specific to least specific order)
        suffixToRootSuffixMap = new LinkedHashMap<>();
        //Remove suffix and add letters back to find root
        suffixToRootSuffixMap.put("PZL", "AZ");
        suffixToRootSuffixMap.put("EZL", "R");
        suffixToRootSuffixMap.put("ZL", "A");

        //Remove suffix to find the root
        suffixToRootSuffixMap.put("L", "");
        suffixToRootSuffixMap.put("LZ", "");
        suffixToRootSuffixMap.put("EVM", "");
        suffixToRootSuffixMap.put("ZQ", "");
    }

    public List<WordPairDto> processWords(WordProcessorDto wordProcessorDto) {

        if (wordProcessorDto == null ||
                CollectionUtils.isEmpty(wordProcessorDto.getWordToProcessList())) {
            return Collections.emptyList();
        }

        final List<WordPairDto> resultPairList = new ArrayList<>();
        final boolean isIncludeStopWords = wordProcessorDto.isIncludeStopWords();
        final boolean isGroupStemWords = wordProcessorDto.isGroupStemWords();

        boolean isEndingFound = false;
        WordPairDto wordPairDto;

        for (String wordToProcess : wordProcessorDto.getWordToProcessList()) {

            //Filter out any stop words if caller desires that they be omitted
            if (isStopWord(wordToProcess) && !isIncludeStopWords) {
                log.info("Found Stop word to ignore: {}", wordToProcess);
                continue;
            }

            log.info("Processing word: {}", wordToProcess);
            //Process the word according to the rules defined in the suffix map
            for (Map.Entry<String, String> entry : suffixToRootSuffixMap.entrySet()) {
                String suffix = entry.getKey();
                String rootSuffix = entry.getValue();

                log.info("Checking word: {} suffix: {} rootSuffix {}", wordToProcess, suffix, rootSuffix);

                isEndingFound = wordToProcess.endsWith(suffix);
                log.info("word: {} isEndingFound: {}", wordToProcess, isEndingFound);
                if (isEndingFound) {
                    wordPairDto = buildWordPair(wordToProcess, suffix, rootSuffix, isGroupStemWords);
                    resultPairList.add(wordPairDto);
                    break;
                }
            }

            //No transformation needed so just the add the word to the result list
            if (!isEndingFound) {
                log.info("No transformation, adding word: {}", wordToProcess);
                wordPairDto = new WordPairDto(wordToProcess, 1L);
                resultPairList.add(wordPairDto);
            }
        }
        return resultPairList;
    }


    private boolean isStopWord(String word) {
        return StringUtils.isNotEmpty(word) && stopWordSet.contains(word);
    }

    private WordPairDto buildWordPair(String word, String suffix, String suffixToRoot, boolean isGroupStemWords) {

        log.info("word: {} suffix: {} suffixRoot: {} isGroupStemWords: {}", word, suffix, suffixToRoot, isGroupStemWords);

        int index = word.lastIndexOf(suffix);
        String stemWord = word.substring(0, index);

        log.info("word: {} stemWord: {}", word, stemWord);

        StringBuffer resultWordBuffer = new StringBuffer(stemWord);

        //Append the appropriate suffix (transformed or original) if caller desires no grouping
        if (!isGroupStemWords) {
            if (StringUtils.isNotEmpty(suffixToRoot)) {
                resultWordBuffer.append(suffixToRoot);
            } else {
                resultWordBuffer.append(suffix);
            }
        }

        log.info("word: {} resultWord: {}", word, resultWordBuffer.toString());

        WordPairDto wordPairDto = new WordPairDto(resultWordBuffer.toString(), 1L);
        return wordPairDto;
    }
}
