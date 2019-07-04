package com.topica.threadpool;

public class CreateRequest {
    private static final String STATUS_WAITING = "WAITING";
    private static final Integer TIME_SLEEP = 1000;
    private Integer listSizeThread;
    private RequestExecutor executor;

    public void create(int sizeRequest) throws InterruptedException {
        for (int numberOfRequest = 0; numberOfRequest < sizeRequest; numberOfRequest++) {
            listSizeThread = BlockingThreadList.getRequestExecutors().size();
            RequestHandler requestHandler = new RequestHandler("Request " + numberOfRequest);

            for (int index = 0; index < listSizeThread; index++) {
                executor = BlockingThreadList.getRequestExecutors().get(index);
                if (isWaiting()) {
                    handleThreadWaiting(requestHandler);
                    break;
                }
                if (index == listSizeThread - 1) {
                    handleThreadRunning(requestHandler);
                }
            }
            Thread.sleep(TIME_SLEEP);
        }
        System.out.println("Size queue :" + BlockingRequestQueue.getQueue().size());
        getRequestFromQueue();
    }

    private void handleThreadWaiting(RequestHandler requestHandler) throws InterruptedException {
        synchronized (executor) {
            executor.notify();
            if (BlockingRequestQueue.isEmptyQueue()) {
                requestHandler.setNameThread(executor.getNameThread());
                executor.setRequestHandler(requestHandler);
            } else {
                RequestHandler request = (RequestHandler) BlockingRequestQueue.dequeue();
                request.setNameThread(executor.getNameThread());
                executor.setRequestHandler(request);
                BlockingRequestQueue.enqueue(requestHandler);
            }
        }
    }

    private void handleThreadRunning(RequestHandler requestHandler) throws InterruptedException {
        if (!BlockingRequestQueue.isFullQueue())
            BlockingRequestQueue.enqueue(requestHandler);
        else {
            RequestExecutor thread = new RequestExecutor("Thread-" + listSizeThread);
            BlockingThreadList.addThreadToFactory(thread, (RequestHandler) BlockingRequestQueue.dequeue());
            BlockingRequestQueue.enqueue(requestHandler);
        }
    }

    private void getRequestFromQueue() {
        while (!BlockingRequestQueue.isEmptyQueue()) {
            listSizeThread = BlockingThreadList.getRequestExecutors().size();

            for (int index = 0; index < listSizeThread; index++) {
                executor = BlockingThreadList.getRequestExecutors().get(index);
                if (isWaiting()) {
                    synchronized (executor) {
                        RequestHandler requestHandler = (RequestHandler) BlockingRequestQueue.dequeue();
                        executor.notify();
                        executor.setRequestHandler(requestHandler);
                    }
                    break;
                }
            }
        }
    }

    private Boolean isWaiting() {
        return executor.getState()
                .toString()
                .equals(STATUS_WAITING);
    }
}