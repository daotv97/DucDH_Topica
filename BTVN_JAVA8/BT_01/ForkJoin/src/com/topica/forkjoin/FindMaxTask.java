package com.topica.forkjoin;

import java.util.concurrent.RecursiveTask;

//
// Task class.  Represents a range of an array to be searched
// to find the maximum value.
//
public class FindMaxTask extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;

    private int[] arr;
    private int start, end;

    public FindMaxTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= Constant.THRESHOLD) {
            // number of elements is at or below the threshold - compute directly
            return computeDirectly();
        } else {
            // number of elements is above the threshold - split into subtasks
            int mid = start + (end - start) / 2;
            FindMaxTask left = new FindMaxTask(arr, start, mid);
            FindMaxTask right = new FindMaxTask(arr, mid, end);

            // invoke the tasks in parallel and wait for them to complete
            invokeAll(left, right);

            // maximum of overall range is maximum of sub-ranges
            return Math.max(left.join(), right.join());
        }
    }

    private Integer computeDirectly() {
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}
