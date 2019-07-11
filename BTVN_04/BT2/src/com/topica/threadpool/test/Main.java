package com.topica.threadpool.test;

import com.topica.threadpool.api.ThreadPoolExecutor;
import com.topica.threadpool.utils.Constant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(Constant.CAPACITY);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(Constant.CORE_POOL_SIZE, Constant.MAXIMUM_POOL_SIZE, blockingQueue);
        IntStream.rangeClosed(1, Constant.TASKS).mapToObj(i -> new Task("Task " + i)).forEach(task -> {
            executor.execute(task);
            try {
                Thread.sleep(Constant.TIME_CREATE_TASK);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }
}
