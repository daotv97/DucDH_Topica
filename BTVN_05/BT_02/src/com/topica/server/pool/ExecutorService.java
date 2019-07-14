package com.topica.server.pool;

public interface ExecutorService {
    void execute();

    void terminate();
}
