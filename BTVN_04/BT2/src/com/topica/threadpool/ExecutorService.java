package com.topica.threadpool;

public interface ExecutorService {

    void execute(Task task);

    void shutdown();

    boolean isShutdown();

    boolean isTerminated();

}
