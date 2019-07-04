package com.topica.threadpool;

public class ThreadPoolExecutor {
    private static final Integer REQUEST_QUEUE_SIZE = 8;
    private static final Integer CORE_THREAD_SIZE = 5;
    private static final Integer MAX_THREAD_SIZE = 10;
    private static final Integer SIZE_REQUESTS = 100;

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(REQUEST_QUEUE_SIZE,CORE_THREAD_SIZE, MAX_THREAD_SIZE);
        threadPool.request(SIZE_REQUESTS);
    }
}
