package com.topica.btvn02;

import java.util.Map;

public interface WordProcessor {
    Map<String, Integer> filterWords(String content);

    Map<String, Integer> sortByFrequencyDesc(Map<String, Integer> map);
}
