package com.topica.threadpool;

public class ThreadPool {
    private static Integer coreThreadSize;
    private BlockingRequestQueue blockingRequestQueue;
    private BlockingThreadList blockingThreadList;

    public ThreadPool(int requestQueueSize, int coreThreadSize, int maxThreadSize) {
        ThreadPool.coreThreadSize = coreThreadSize;
        blockingRequestQueue = new BlockingRequestQueue(requestQueueSize);
        blockingThreadList = new BlockingThreadList(coreThreadSize, maxThreadSize);
        init();
    }

    private static void init() {
        for (int index = 0; index < coreThreadSize; index++) {
            RequestExecutor executor = new RequestExecutor("Thread-" + index);
            BlockingThreadList.getRequestExecutors().add(executor);
            BlockingThreadList.getRequestExecutors().get(index).start();
        }
    }

    public void request(int sizeRequest) {
        try {
            new CreateRequest().create(sizeRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
