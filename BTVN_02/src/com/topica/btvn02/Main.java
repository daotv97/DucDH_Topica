package com.topica.btvn02;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

public class Main {
    private static final String PATH_FILE = "E:\\Topica\\DucDH_Topica\\BTVN_02\\BTVN-5.txt";

    public static void main(String[] args) {
        Long start = Calendar.getInstance().getTimeInMillis();
        WordReader statistical = new WordReader();
        try {
            Optional.ofNullable(statistical.read(PATH_FILE))
                    .ifPresent(s -> {
                        Map<String, Integer> map = statistical.filterWords(s);
                        statistical.sortByFrequencyAsc(map).forEach((e, v) -> {
                            System.out.println(e + ": " + v);
                        });
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("=> Time: " + (end - start) * 1.0 / 1000);
    }
}
