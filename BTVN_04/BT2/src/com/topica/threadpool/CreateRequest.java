package com.topica.threadpool;

public class CreateRequest extends Thread {
    private static final String STATUS_WAITING = "WAITING";
    private Integer listSizeThread;
    private FactoryThread thread;

    @Override
    public void run() {
        int numberOfRequest = 0;
        RequestHandler requestHandler;
        while (numberOfRequest < 20) {
            requestHandler = new RequestHandler("request " + numberOfRequest);
            listSizeThread = BlockingThreadList.getFactoryThreads().size();

            for (int index = 0; index < listSizeThread; index++) {
                thread = BlockingThreadList.getFactoryThreads().get(index);
                String state = thread.getState().toString();

                if (state.equals(STATUS_WAITING)) {
                    synchronized (thread) {
                        requestHandler.setNameThread(thread.getNameThread());
                        thread.notify();
                        thread.setRequestHandler(requestHandler);
                    }
                    break;

                } else if (index == listSizeThread - 1) {
                    try {
                        boolean isMaxQueueSize = BlockingRequestQueue.enqueue(requestHandler);
                        if (!isMaxQueueSize) {
                            FactoryThread thread = new FactoryThread("Thread-" + listSizeThread);
                            BlockingThreadList.addThreadToQueue(thread, requestHandler);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            numberOfRequest++;
        }
        System.out.println("size queue :" + BlockingRequestQueue.getQueue().size());
        while (!BlockingRequestQueue.getQueue().isEmpty()) {
            listSizeThread = BlockingThreadList.getFactoryThreads().size();
            for (int index = 0; index < listSizeThread; index++) {
                thread = BlockingThreadList.getFactoryThreads().get(index);
                String state = thread.getState().toString();
                if (state.equals(STATUS_WAITING)) {
                    synchronized (thread) {
                        requestHandler = (RequestHandler) BlockingRequestQueue.dequeue();
                        requestHandler.setNameThread(thread.getNameThread());
                        thread.notify();
                        thread.setRequestHandler(requestHandler);
                    }
                    break;
                }

            }
        }
    }
}
