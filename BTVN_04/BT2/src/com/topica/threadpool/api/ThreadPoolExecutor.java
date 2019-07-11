package com.topica.threadpool.api;

import com.topica.threadpool.utils.Constant;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ThreadPoolExecutor implements ExecutorService {

    private final BlockingQueue<Runnable> workQueue;
    private final ArrayList<Worker> workers;
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private boolean active = false;

    /**
     * Constructor for ThreadPoolExecutor class and initialize thread pool.
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param workQueue
     */
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

    /**
     * Initialize threads with the number of <b>corePoolSize</b>,
     * add them to the thread pool and start.
     *
     * @param workers
     * @param corePoolSize
     */
    private void initializeThread(ArrayList<Worker> workers, int corePoolSize) {
        IntStream.range(0, corePoolSize).forEach(index -> {
            workers.add(new Worker("Thread-" + index));
            workers.get(index).start();
        });
    }

    /**
     * Check if any thread is in <b>waiting</b> state.
     *
     * @param worker
     * @return true if this thread is in <b>waiting</b> state.
     */
    private boolean isThreadWorkerWaiting(Worker worker) {
        return Constant.STATUS_WAITING.equals(worker.getState().toString());
    }

    /**
     * If one of the initialized threads is in a <b>waiting</b> state
     * and in the queue list is empty,
     * this thread will take the requested task.
     * <p>
     * If one of the threads is initialized in the <b>waiting</b> state
     * and in the queue list is not empty,
     * the thread will retrieve a task in the queue to process
     * and then add the new task to the queue.
     *
     * @param worker
     * @param task
     */
    private void handleThreadIsWaiting(Worker worker, Runnable task) {
        synchronized (worker) {
            if (workQueue.isEmpty()) {
                worker.notify();
                worker.setRunnable(task);
            } else {
                workQueue.add(task);
                worker.notify();
                worker.setRunnable(workQueue.poll());
            }
        }
    }

    /**
     * When the list contains threads that handle tasks
     * without any thread in the <b>waiting</b> state and the number of threads
     * in that list is smaller than <b>maxPoolSize</b>,
     * a new thread will be added to that list and will handle the new task.
     * <p>
     * If the number of threads in that list is equal to <b>maxPoolSize</b>,
     * it will reject execution.
     *
     * @param task
     * @param index
     */
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
                System.out.println("Reject execution!");
            }
        }
    }

    /**
     * If there is a thread in the list that is in the <b>waiting</b> state,
     * it will call <b>handleThreadIsWaiting</b> method
     * <p>
     * If the list does not find any thread that is in <b>waiting</b> state,
     * it will call the method <b>handleThreadIsRunningAll</b>
     *
     * @param runnable
     */
    private void handle(Runnable runnable) {
        int currentThreadSize = workers.size();
        for (int index = 0; index < currentThreadSize; index++) {
            Worker worker = workers.get(index);
            // If there is a thread in the list that is in the 'waiting' state.
            if (isThreadWorkerWaiting(worker)) {
                handleThreadIsWaiting(worker, runnable);
                break;
            }
            // If the list does not find any thread that is in 'waiting' state.
            if (index == currentThreadSize - 1) {
                handleThreadIsRunningAll(runnable, index);
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (!active) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            handle(runnable);
        }
    }


    @Override
    public void shutdown() {
        this.active = true;
    }

    @Override
    public boolean isShutdown() {
        return active;
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

    /**
     * Getter for property <b>corePoolSize</b>.
     *
     * @return Value for property <b>corePoolSize</b>.
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * Setter for property <b>corePoolSize</b>.
     *
     * @param corePoolSize Value to set for property <b>corePoolSize</b>.
     */
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * Getter for property <b>maximumPoolSize</b>.
     *
     * @return Value for property <b>maximumPoolSize</b>.
     */
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * Setter for property <b>maximumPoolSize</b>.
     *
     * @param maximumPoolSize Value to set for property <b>maximumPoolSize</b>.
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * Getter for property <b>workQueue</b>.
     *
     * @return Value for property <b>workQueue</b>.
     */
    public BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    private final class Worker extends Thread {
        private String nameThread;
        private Runnable runnable;

        /**
         * Constructor for Worker class.
         *
         * @param nameThread
         */
        public Worker(String nameThread) {
            this.nameThread = nameThread;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            while (true) {
                synchronized (this) {
                    try {
                        if (runnable == null) {
                            if (workQueue.isEmpty()) {
                                this.wait();
                            } else {
                                runnable = workQueue.poll();
                                runnable.run();
                                runnable = null;
                            }
                        } else {
                            this.runnable.run();
                            this.runnable = null;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                IntStream.range(0, workers.size())
                        .filter(index -> workQueue.isEmpty() && (workers.size() > corePoolSize) && isThreadWorkerWaiting(workers.get(index)))
                        .forEachOrdered(index -> {
                    workers.get(index).interrupt();
                    if (workers.get(index).isInterrupted()) {
                        workers.remove(index);
                    }
                });
            }
        }

        /**
         * Getter for property <b>runnable</b>.
         *
         * @return Value for property <b>runnable</b>.
         */
        public Runnable getRunnable() {
            return runnable;
        }

        /**
         * Setter for property <b>runnable</b>.
         *
         * @param runnable Value to set for property 'runnable'.
         */
        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        /**
         * Getter for property <b>nameThread</b>.
         *
         * @return Value for property <b>nameThread</b>.
         */
        public String getNameThread() {
            return nameThread;
        }

        /**
         * Setter for property <b>nameThread</b>.
         *
         * @param nameThread Value to set for property <b>nameThread</b>.
         */
        public void setNameThread(String nameThread) {
            this.nameThread = nameThread;
        }
    }
}