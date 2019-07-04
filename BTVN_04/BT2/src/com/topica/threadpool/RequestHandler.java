package com.topica.threadpool;

/**
 * <h1>RequestHandler</h1>
 * This program is responsible for handling threads and tasks.
 * When the thread is in standby mode, the thread must retrieve new tasks or tasks that are waiting in the queue to process.
 * When in the list of threads there are no threads in standby mode, it must be added to the queue list.
 *
 * @author Dao Huy Duc, Mai Trong Nghia, Nguyen Tien Duy.
 * @version 1.2
 * @since 2019-07-04
 */
public class RequestHandler {
    private static Integer listSizeThread;
    private static RequestExecutor executor;

    /**
     * <b>Constructor</b> for the RequestHandler class.
     * Set size for 'listSizeThread'.
     */
    public RequestHandler() {
        RequestHandler.listSizeThread = BlockingThreadList.getRequestExecutors().size();
    }

    /**
     * This method handles instances where the thread is in a pending state.
     *
     * @param requestHandler
     */
    private static void handleThreadWaiting(Request requestHandler) {
        synchronized (executor) {
            executor.notify();
            if (BlockingRequestQueue.isEmptyQueue()) {
                requestHandler.setNameThread(executor.getNameThread());
                executor.setRequest(requestHandler);
            } else {
                Request request = (Request) BlockingRequestQueue.dequeue();
                request.setNameThread(executor.getNameThread());
                executor.setRequest(request);

                BlockingRequestQueue.enqueue(requestHandler);
            }
        }
    }

    /**
     * @param request
     */
    private static void handleThreadRunning(Request request) {
        if (!BlockingRequestQueue.isFullQueue()) BlockingRequestQueue.enqueue(request);
        else {
            RequestExecutor thread = new RequestExecutor("Thread-" + listSizeThread);
            BlockingThreadList.addThreadToFactory(thread, (Request) BlockingRequestQueue.dequeue());
            BlockingRequestQueue.enqueue(request);
        }
    }

    /**
     *
     */
    private static void getRequestFromQueue() {
        while (!BlockingRequestQueue.isEmptyQueue()) {
            listSizeThread = BlockingThreadList.getRequestExecutors().size();

            for (int index = 0; index < listSizeThread; index++) {
                executor = BlockingThreadList.getRequestExecutors().get(index);
                if (isWaiting()) {
                    synchronized (executor) {
                        executor.notify();
                        Request request = (Request) BlockingRequestQueue.dequeue();
                        request.setNameThread(executor.getNameThread());
                        executor.setRequest(request);
                    }
                    break;
                }
            }
        }
    }

    /**
     * @return
     */
    private static Boolean isWaiting() {
        return executor.getState()
                .toString()
                .equals(Constant.STATUS_WAITING);
    }

    /**
     * @param sizeRequest
     * @throws InterruptedException
     */
    public void handle(int sizeRequest) throws InterruptedException {
        for (int numberOfRequest = 0; numberOfRequest < sizeRequest; numberOfRequest++) {
            listSizeThread = BlockingThreadList.getRequestExecutors().size();
            Request request = new Request("Request " + numberOfRequest);

            for (int index = 0; index < listSizeThread; index++) {
                executor = BlockingThreadList.getRequestExecutors().get(index);
                if (isWaiting()) {
                    handleThreadWaiting(request);
                    break;
                }
                if (index == listSizeThread - 1) handleThreadRunning(request);
            }
            Thread.sleep(Constant.TIME_CREATING);
        }
        System.out.println("Size queue :" + BlockingRequestQueue.getQueue().size());
        getRequestFromQueue();
    }
}