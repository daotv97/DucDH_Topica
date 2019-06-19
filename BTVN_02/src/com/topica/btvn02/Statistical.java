package com.topica.btvn02;

import java.io.*;
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

    public void readFile(String path) throws IOException{
        String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8)
                .replaceAll("[,.;]", "");
        String[] listWords = content.split(" ");
        String key="";
        Integer value;
        for (int i = 0; i<=listWords.length; i++) {
            key = listWords[i];
            if(numberOfWords.containsKey(key)){
                value =  numberOfWords.get(key);
                numberOfWords.remove(key);
                numberOfWords.put(key, value+1);
            } else{
                numberOfWords.put(key, new Integer(1));
            }
        }
        System.out.println("haa");

    }

    public int statistical() {
        return 0;
    }
}
