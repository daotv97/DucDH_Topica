package com.topica.threadpool;

import java.util.ArrayList;

public class BlockingThreadList {
    private static ArrayList<RequestExecutor> requestExecutors;
    private static Integer coreSize;
    private static Integer maxSize;

    public BlockingThreadList(int core, int max) {
        requestExecutors = new ArrayList<>();
        coreSize = core;
        maxSize = max;
    }

    public static synchronized Boolean addThreadToFactory(RequestExecutor thread, RequestHandler requestHandler) {
        if (requestExecutors.size() >= coreSize && requestExecutors.size() < maxSize) {
            requestExecutors.add(thread);
            requestHandler.setNameThread(thread.getNameThread());
            thread.setRequestHandler(requestHandler);
            thread.start();
            return true;
        }
        return false;
    }

    public static Boolean isFullThread() {
        return requestExecutors.size() == maxSize;
    }

    public static void resetPoolSize() {
        for (int index = 0; requestExecutors.size() > Constant.CORE_THREAD_SIZE; index++)
            if (Constant.STATUS_WAITING.equals(requestExecutors.get(index).getState().toString())) {
                requestExecutors.get(index).interrupt();
                requestExecutors.remove(index);
            }
    }

    public static ArrayList<RequestExecutor> getRequestExecutors() {
        return requestExecutors;
    }

    public static void setRequestExecutors(ArrayList<RequestExecutor> requestExecutors) {
        BlockingThreadList.requestExecutors = requestExecutors;
    }
}