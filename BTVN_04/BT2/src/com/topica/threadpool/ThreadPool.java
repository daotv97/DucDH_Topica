package com.topica.threadpool;

public class ThreadPool {
    private static Integer coreThreadSize;
    private BlockingRequestQueue blockingRequestQueue;
    private BlockingThreadList blockingThreadList;

    public ThreadPool(int requestQueueSize, int coreThreadSize, int maxThreadSize) {
        ThreadPool.coreThreadSize = coreThreadSize;
        this.blockingRequestQueue = new BlockingRequestQueue(requestQueueSize);
        this.blockingThreadList = new BlockingThreadList(coreThreadSize, maxThreadSize);
        init();
    }

    private static void init() {
        for (int index = 0; index < coreThreadSize; index++) {
            RequestExecutor executor = new RequestExecutor("Thread-" + index);
            BlockingThreadList.getRequestExecutors().add(executor);
            BlockingThreadList.getRequestExecutors().get(index).start();
        }
    }

    public void request(int sizeRequest) throws InterruptedException {
        new CreateRequest().create(sizeRequest);
    }
}