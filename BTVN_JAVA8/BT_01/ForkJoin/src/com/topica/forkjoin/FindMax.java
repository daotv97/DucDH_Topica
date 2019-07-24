package com.topica.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class FindMax {
    //
    // Threshold value: when a range of the array contains more than
    // this many elements, we will fork tasks to divide the array
    // into smaller ranges.
    //

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // create large array of random integer values
        int[] data = Util.makeRandomArray(Constant.ARR_SIZE);

        ForkJoinPool pool = new ForkJoinPool();
        FindMaxTask rootTask = new FindMaxTask(data, 0, data.length);
        Integer result = pool.invoke(rootTask);

        System.out.println("Max is " + result);

        // sequentially verify that result is correct
        if (result.intValue() != Util.findMax(data)) {
            System.out.println("ERROR: parallel search did not find correct maximum");
        }
    }
}
