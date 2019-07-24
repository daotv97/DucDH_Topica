package com.topica.forkjoin;

import java.util.Random;

public abstract class Util {
    public static int[] makeRandomArray(int arrSize) {
        Random rand = new Random(123L);
        int[] data = new int[arrSize];
        for (int i = 0; i < data.length; i++) {
            data[i] = rand.nextInt();
        }
        return data;
    }

    public static boolean isSorted(int[] data) {
        int last = Integer.MIN_VALUE;
        for (int value : data) {
            if (value < last) {
                return false;
            }
            last = value;
        }
        return true;
    }

    public static Integer findMax(int[] data) {
        int max = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
