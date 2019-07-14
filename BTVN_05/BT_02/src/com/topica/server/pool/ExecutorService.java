package com.topica.server.pool;

public interface ExecutorService {
    void execute(Runnable runnable);

    void shutdown();

    boolean isShutdown();

    boolean isTerminated();

    boolean isTerminating();

    boolean remove(Runnable runnable);
}
