package com.topica.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, blockingQueue);
        IntStream.rangeClosed(1, 5).mapToObj(i -> new Task("Task " + i)).forEach(task -> {
            System.out.println("Created : " + task.getName());
            executor.execute(task);
        });
    }
}
