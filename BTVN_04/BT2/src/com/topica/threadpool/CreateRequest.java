package com.topica.threadpool;

public class CreateRequest {
    private static Integer listSizeThread;
    private static RequestExecutor executor;

    public CreateRequest() {
        CreateRequest.listSizeThread = BlockingThreadList.getRequestExecutors().size();
    }

    private static void handleThreadWaiting(RequestHandler requestHandler) {
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

    private static void handleThreadRunning(RequestHandler requestHandler) {
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
                        executor.notify();
                        RequestHandler requestHandler = (RequestHandler) BlockingRequestQueue.dequeue();
                        requestHandler.setNameThread(executor.getNameThread());
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
                if (index == listSizeThread - 1) handleThreadRunning(requestHandler);
            }
            Thread.sleep(Constant.TIME_CREATING);
        }
        System.out.println("Size queue :" + BlockingRequestQueue.getQueue().size());
        getRequestFromQueue();
    }
}