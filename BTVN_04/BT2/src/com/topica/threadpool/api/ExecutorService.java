package com.topica.threadpool.api;

public interface ExecutorService {

    void execute(Runnable runnable);

    void shutdown();

    boolean isShutdown();

    boolean isTerminated();

    boolean isTerminating();

    boolean remove(Runnable runnable);
}
