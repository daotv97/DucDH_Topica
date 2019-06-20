package com.topica.btvn02;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Statistical {
    private Map<String, Integer> numberOfWords;

    public Statistical() {
        numberOfWords = new HashMap<>();
    }

    public String readFileAndGetContent(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8)
                .replaceAll("\\W", " ");
    }

    public void statistical(String content) {
        String[] listWords = content.split("\\s+");
        String key = "";
        Integer value;
        for (int i = 0; i < listWords.length; i++) {
            key = listWords[i];
            if (numberOfWords.containsKey(key)) {
                value = numberOfWords.get(key);
                numberOfWords.remove(key);
                numberOfWords.put(key, value + 1);
            } else {
                numberOfWords.put(key, new Integer(1));
            }
        }
    }

    public void sort() {
        numberOfWords.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }
}
