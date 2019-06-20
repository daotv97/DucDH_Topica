package com.topica.btvn02;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WordReader extends FileTxtReader implements WordProcessor {

    @Override
    public void close() throws IOException {

    }

    @Override
    public Map<String, Integer> filterWords(String content) {
        Map<String, Integer> numberOfWords = new HashMap<>();
        String[] listWords = content.split("\\s+");
        Integer value;
        for (String word : listWords) {
            if (numberOfWords.containsKey(word)) {
                value = numberOfWords.get(word);
                numberOfWords.remove(word);
                numberOfWords.put(word, value + 1);
            } else {
                numberOfWords.put(word, new Integer(1));
            }
        }
        return numberOfWords;
    }

    @Override
    public Map<String, Integer> sortByFrequencyAsc(Map<String, Integer> mapWorks) {
        return mapWorks.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    }

}
