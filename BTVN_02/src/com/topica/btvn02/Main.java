package com.topica.btvn02;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

public class Main {
    private static final String PATH_FILE = "E:\\Topica\\DucDH_Topica\\BTVN_02\\BTVN-5.txt";

    public static void main(String[] args) {
        Long start = Calendar.getInstance().getTimeInMillis();
        Statistical statistical = new Statistical();
        try {
            Optional.ofNullable(statistical.readFileAndGetContent(PATH_FILE))
                    .ifPresent(s -> {
                        statistical.statistical(s);
                        statistical.sort();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("=> Time: "+(end-start)*1.0/1000);
    }
}
