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
            workers.add(new Worker("Thread-" + index));
            workers.get(index).start();
        });
    }

    private boolean isWaiting(Worker worker) {
        return worker.getState().toString().equals(Constant.STATUS_WAITING);
    }

    private void handleThreadIsWaiting(Worker worker, Task task) {
        synchronized (worker) {
            worker.notify();
            worker.setTask(task);
        }
    }

    private void handleThreadIsRunningAll(Task task, int index) {
        int workThreadSize = workers.size();
        System.out.println("Size: " + workThreadSize);
        if (workThreadSize < maximumPoolSize) {
            workers.add(new Worker("Thread-" + index));
            workers.get(workThreadSize).start();
        }
        if (workThreadSize == maximumPoolSize) {
            try {
                workQueue.add(task);
            } catch (Exception e) {
                System.out.println("Task " + task.getName() + " bi tu choi truy cap");
            }
        }
    }

    private void handleExecute(Task task) {
        System.out.println("Size thread: " + workers.size() + "-- Size queue: " + workQueue.size());
        int sizeCurrentThread = workers.size();
        for (int index = 0; index < sizeCurrentThread; index++) {
            Worker worker = workers.get(index);

            if (isWaiting(worker)) {
                handleThreadIsWaiting(worker, task);
                break;
            }

            if (index == sizeCurrentThread - 1) {
                handleThreadIsRunningAll(task, index);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Task task) {
        if (task == null) {
            throw new NullPointerException();
        } else {
            handleExecute(task);
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

    private final class Worker extends Thread {
        private String nameThread;
        private Task task;

        public Worker(String nameThread) {
            this.nameThread = nameThread;
        }

        public void run() {

            while (true) {
                synchronized (this) {
                    try {
                        if (task == null) {
                            this.wait();
                        } else {
                            this.task.run();
                            this.task = null;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public String getNameThread() {
            return nameThread;
        }

        public void setNameThread(String nameThread) {
            this.nameThread = nameThread;
        }
    }
}