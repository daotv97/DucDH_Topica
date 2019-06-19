package com.topica.btvn02;

import java.io.IOException;

public class Main {
    private static final String PATH_FILE = "E:\\Topica\\DucDH_Topica\\BTVN_02\\BTVN-5.txt";

    public static void main(String[] args) {
        Statistical statistical = new Statistical();
        try {
            statistical.readFile(PATH_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
