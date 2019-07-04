package com.topica.threadpool;

import java.util.ArrayList;

public class BlockingThreadList {
    private static ArrayList<RequestExecutor> requestExecutors;
    private static Integer coreSize;
    private static Integer maxSize;

    public BlockingThreadList(int core, int max) {
        requestExecutors = new ArrayList();
        coreSize = core;
        maxSize = max;
    }

    public static synchronized Boolean addThreadToFactory(RequestExecutor thread, RequestHandler requestHandler) throws InterruptedException {
        if (requestExecutors.size() < maxSize) {
            requestExecutors.add(thread);
            requestHandler.setNameThread(thread.getNameThread());
            thread.setRequestHandler(requestHandler);
            return true;
        }
        return false;
    }

    public static ArrayList<RequestExecutor> getRequestExecutors() {
        return requestExecutors;
    }

    public static void setRequestExecutors(ArrayList<RequestExecutor> requestExecutors) {
        BlockingThreadList.requestExecutors = requestExecutors;
    }

    public static Boolean isFullThread() {
        return requestExecutors.size() == maxSize;
    }

    public Boolean shutdown() {
        for (int index = 0; index < maxSize; index++) {
            if (!requestExecutors.get(index).getState().toString().equals("WAITING")) {
                return false;
            }
        }
        return true;
    }
}
