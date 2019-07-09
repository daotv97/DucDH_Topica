package com.topica.threadpool;

public interface ExecutorService {

    void shutdown();

    boolean isShutdown();

    boolean isTerminated();

}
