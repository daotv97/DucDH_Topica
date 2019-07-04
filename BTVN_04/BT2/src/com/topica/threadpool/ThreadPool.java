package com.topica.threadpool;

/**
 * <h1>ThreadPool</h1>
 * The main program of the entire application.
 * Create a queue with a fixed size.
 * Initialize the ThreadPoolExecutor class and run the request.
 * The output on the screen console.
 * <b>Note:</b> None.
 *
 * @author Dao Huy Duc, Mai Trong Nghia, Nguyen Tien Duy.
 * @version 1.2
 * @since 2019-07-04
 */
public class ThreadPool {

    /**
     * <p>This main method is used to initialize
     * and run the ThreadPoolExecutor class requirements.
     * Initialize the queue list for requests.</p>
     *
     * @param args Unused.
     * @return Nothing.
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new BlockingRequestQueue(Constant.REQUEST_QUEUE_SIZE);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Constant.CORE_THREAD_SIZE, Constant.MAX_THREAD_SIZE);
        threadPoolExecutor.request(Constant.SIZE_REQUESTS);

        BlockingThreadList.reset();
    }
}