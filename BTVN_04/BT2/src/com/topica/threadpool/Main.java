package com.topica.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue(Constant.CAPACITY);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(Constant.CORE_POOL_SIZE, Constant.MAXIMUM_POOL_SIZE, blockingQueue);
        IntStream.rangeClosed(1, Constant.TASKS).mapToObj(i -> new Task("Task " + i)).forEach(task -> {
            System.out.println("Created : " + task.getName());
            executor.execute(task);
        });
    }
}
