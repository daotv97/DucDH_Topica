package com.topica.threadpool.api;

import com.topica.threadpool.test.Task;

public interface ExecutorService {

    void execute(Task task);

    void shutdown();

    boolean isShutdown();

    boolean isTerminated();

}
