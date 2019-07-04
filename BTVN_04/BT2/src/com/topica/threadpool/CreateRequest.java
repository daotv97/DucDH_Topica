package com.topica.threadpool;

public class CreateRequest {
    private static Integer listSizeThread = BlockingThreadList.getRequestExecutors().size();
    private static RequestExecutor executor;

    private static void handleThreadWaiting(RequestHandler requestHandler) throws InterruptedException {
        synchronized (executor) {
            executor.notify();
            if (!BlockingRequestQueue.isEmptyQueue()) {
                RequestHandler request = (RequestHandler) BlockingRequestQueue.dequeue();
                request.setNameThread(executor.getNameThread());
                executor.setRequestHandler(request);
                BlockingRequestQueue.enqueue(requestHandler);
            } else {
                requestHandler.setNameThread(executor.getNameThread());
                executor.setRequestHandler(requestHandler);
            }
        }
    }

    private static void handleThreadRunning(RequestHandler requestHandler) throws InterruptedException {
        if (!BlockingRequestQueue.isFullQueue()) BlockingRequestQueue.enqueue(requestHandler);
        else {
            RequestExecutor thread = new RequestExecutor("Thread-" + listSizeThread);
            BlockingThreadList.addThreadToFactory(thread, (RequestHandler) BlockingRequestQueue.dequeue());
            BlockingRequestQueue.enqueue(requestHandler);
        }
    }

    private static void getRequestFromQueue() {
        while (!BlockingRequestQueue.isEmptyQueue()) {
            listSizeThread = BlockingThreadList.getRequestExecutors().size();

            for (int index = 0; index < listSizeThread; index++) {
                executor = BlockingThreadList.getRequestExecutors().get(index);
                if (isWaiting()) {
                    synchronized (executor) {
                        RequestHandler requestHandler = (RequestHandler) BlockingRequestQueue.dequeue();
                        requestHandler.setNameThread(executor.getNameThread());
                        executor.notify();
                        executor.setRequestHandler(requestHandler);
                    }
                    break;
                }
            }
        }
    }

    private static Boolean isWaiting() {
        return executor.getState()
                .toString()
                .equals(Constant.STATUS_WAITING);
    }

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
            Thread.sleep(Constant.TIME_CREATING);
        }
        System.out.println("Size queue :" + BlockingRequestQueue.getQueue().size());
        getRequestFromQueue();

        // Test code
        for (int i = 0; i < listSizeThread; i++) {
            System.out.println(BlockingThreadList.getRequestExecutors().get(i).getNameThread()
                    + " - "
                    + BlockingThreadList.getRequestExecutors().get(i).getState());
        }
    }
}