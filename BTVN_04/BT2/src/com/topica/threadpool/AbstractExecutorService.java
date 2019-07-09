package com.topica.threadpool;

public abstract class AbstractExecutorService implements ExecutorService {

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }
}
