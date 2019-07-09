package com.topica.threadpool;

import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, blockingQueue);
        for (int i = 1; i <= 5; i++)
        {
            Task task = new Task("Task " + i);
            System.out.println("Created : " + task.getName());

            executor.execute(task);
        }
    }
}
