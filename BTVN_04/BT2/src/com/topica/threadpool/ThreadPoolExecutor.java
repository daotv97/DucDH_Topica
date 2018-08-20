package com.topica.threadpool;

public class ThreadPoolExecutor {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(8,3);
        CreateRequest createRequest = new CreateRequest();
        createRequest.start();
    }
}
