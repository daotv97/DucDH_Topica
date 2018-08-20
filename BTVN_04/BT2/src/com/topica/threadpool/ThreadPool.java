package com.topica.threadpool;

public class ThreadPool {
    private BlockingRequestQueue blockingRequestQueue;

    public ThreadPool(int requestQueueSize, int coreThreadSize) {
        blockingRequestQueue = new BlockingRequestQueue(requestQueueSize);
        String threadName = "";
        for (int count = 0; count < coreThreadSize; count++) {
            threadName = "Thread-" + count;
            FactoryThread thread = new FactoryThread(threadName);
            BlockingThreadList.getFactoryThreads().add(thread);
            BlockingThreadList.getFactoryThreads().get(count).start();
        }
    }
}
