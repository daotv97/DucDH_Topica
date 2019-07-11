package com.topica.threadpool.api;

import com.topica.threadpool.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ThreadPoolExecutor implements ExecutorService {

    private final BlockingQueue<Runnable> workQueue;
    private final ArrayList<Worker> workers;
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue) {
        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize) {
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

    private boolean isThreadWorkerWaiting(Worker worker) {
        return Constant.STATUS_WAITING.equals(worker.getState().toString());
    }

    private void handleThreadIsWaiting(Worker worker, Runnable task) {
        synchronized (worker) {
            if (workQueue.isEmpty()) {
                worker.notify();
                worker.setRunnable(task);
            } else {
                Runnable taskInQueue = workQueue.poll();
                workQueue.add(task);
                worker.notify();
                worker.setRunnable(taskInQueue);
                System.out.println(workQueue.size());
            }
        }
    }

    private void handleThreadIsRunningAll(Runnable task, int index) {
        int workThreadSize = workers.size();
        if (workThreadSize < maximumPoolSize) {
            Worker worker = new Worker("Thread-" + index);
            worker.setRunnable(task);
            workers.add(worker);
            workers.get(workThreadSize).start();
        }
        if (workThreadSize == maximumPoolSize) {
            try {
                workQueue.add(task);
            } catch (Exception e) {
                System.out.println("bi tu choi xu ly!");
            }
        }
    }

    private void handle(Runnable runnable) {
        int currentThreadSize = workers.size();
        for (int index = 0; index < currentThreadSize; index++) {
            Worker worker = workers.get(index);

            if (isThreadWorkerWaiting(worker)) {
                handleThreadIsWaiting(worker, runnable);
                break;
            }

            if (index == currentThreadSize - 1) {
                handleThreadIsRunningAll(runnable, index);
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        handle(runnable);
    }

    @Override
    public void shutdown() {
        System.out.println("Shutting down thread pool");
        IntStream.range(0, workers.size()).forEach(index -> workers.add(index, null));
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }


    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean isTerminating() {
        return false;
    }

    @Override
    public boolean remove(Runnable runnable) {
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

    public BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    private final class Worker extends Thread {
        private String nameThread;
        private Runnable runnable;

        public Worker(String nameThread) {
            this.nameThread = nameThread;
        }

        public void run() {
            while (true) {
                synchronized (this) {
                    try {
                        if (runnable == null) {
                            if (!workQueue.isEmpty()) {
                                runnable = workQueue.poll();
                                runnable.run();
                                runnable = null;
                            } else {
                                this.wait();
                            }
                        } else {
                            this.runnable.run();
                            this.runnable = null;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(new StringBuilder()
                        .append("SIZE THREAD: ")
                        .append(workers.size())
                        .append(" SIZE QUEUE: ")
                        .append(workQueue.size())
                        .toString());
            }
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public String getNameThread() {
            return nameThread;
        }

        public void setNameThread(String nameThread) {
            this.nameThread = nameThread;
        }
    }
}