package com.topica.threadpool;

public interface ExecutorService {

    void execute(Runnable task);

    void shutdown();

    boolean isShutdown();

    boolean isTerminated();

}
