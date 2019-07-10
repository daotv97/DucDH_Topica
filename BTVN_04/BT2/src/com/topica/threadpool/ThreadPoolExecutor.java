package com.topica.threadpool;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ThreadPoolExecutor extends AbstractExecutorService {

    private final BlockingQueue<Runnable> workQueue;
    private final ArrayList<Worker> workers;
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;

    ThreadPoolExecutor(int corePoolSize,
                       int maximumPoolSize,
                       BlockingQueue<Runnable> workQueue) {
        if (corePoolSize < 0 ||
                maximumPoolSize <= 0 ||
                maximumPoolSize < corePoolSize
        ) {
            throw new IllegalArgumentException();
        }
        if (workQueue == null) {
            throw new NullPointerException();
        }
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        workers = new ArrayList<>();
        initializeThread(workers, corePoolSize);
    }

    private void initializeThread(ArrayList<Worker> workers, int corePoolSize) {
        IntStream.range(0, corePoolSize).forEach(index -> {
            workers.add(new Worker());
            workers.get(index).run();
        });
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        synchronized (workQueue) {
            workQueue.add(task);
            workQueue.notify();
        }
    }

    @Override
    public void shutdown() {
        System.out.println("Shutting down thread pool");
        IntStream.range(0, corePoolSize).forEach(index -> workers.add(index, null));
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    private final class Worker implements Runnable {
        public void run() {
            Runnable task;

            while (true) {
                synchronized (workQueue) {
                    while (workQueue.isEmpty()) {
                        try {
                            workQueue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                        }
                    }
                    task = workQueue.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }
}