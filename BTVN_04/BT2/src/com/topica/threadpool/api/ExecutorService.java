package com.topica.threadpool.api;

import java.util.List;

public interface ExecutorService {

    void execute(Runnable runnable);

    void shutdown();

    List<Runnable> shutdownNow();

    boolean isShutdown();

    boolean isTerminated();

    boolean isTerminating();

    boolean remove(Runnable runnable);
}
