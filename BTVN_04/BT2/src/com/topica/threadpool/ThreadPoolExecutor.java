package com.topica.threadpool;

/**
 * <h1>ThreadPoolExecutor</h1>
 * This ThreadPoolExecutor program will <b>builds</b>, <b>initializes</b> threads.
 * Initialize the list of the threads in standby mode.
 * Call threads to handle tasks when it is in standby mode.
 *
 * @author Dao Huy Duc, Mai Trong Nghia, Nguyen Tien Duy.
 * @version 1.2
 * @since 2019-07-04
 */
public class ThreadPoolExecutor {
    private static Integer coreThreadSize;

    /**
     * <b>Constructor</b> for the ThreadPoolExecutor class.
     * This method is responsible for <b>initializing</b> the list of threads and queues that contain the requests.
     * It will call 'init' method to <b>initialize</b> thread and <code>run</code> in standby mode.
     *
     * @param coreThreadSize
     * @param maxThreadSize
     * @return Nothing.
     */
    public ThreadPoolExecutor(int coreThreadSize, int maxThreadSize) {
        ThreadPoolExecutor.coreThreadSize = coreThreadSize;
        new BlockingThreadList(coreThreadSize, maxThreadSize);
        init();
    }

    /**
     * This method will <b>initialize threads</b>, then </code>add</code> them to the list of threads
     * and let them </code>work</code> in standby mode.
     *
     * @return Nothing.
     */
    private static void init() {
        for (int index = 0; index < coreThreadSize; index++) {
            RequestExecutor executor = new RequestExecutor("Thread-" + index);
            executor.start();
            BlockingThreadList.getRequestExecutors().add(executor);
        }
    }

    /**
     * If this method is called, it will call the <b>'handle'</b> method
     * in the RequestHandler class to handle the task.
     *
     * @param sizeRequest
     * @return Nothing.
     * @throws InterruptedException
     * @see InterruptedException
     */
    public void request(int sizeRequest) throws InterruptedException {
        new RequestHandler().handle(sizeRequest);
    }
}