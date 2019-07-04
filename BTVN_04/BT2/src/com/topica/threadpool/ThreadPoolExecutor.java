package com.topica.threadpool;

public class ThreadPoolExecutor {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(Constant.REQUEST_QUEUE_SIZE
                , Constant.CORE_THREAD_SIZE
                , Constant.MAX_THREAD_SIZE);
        threadPool.request(Constant.SIZE_REQUESTS);
    }
}
