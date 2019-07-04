package com.topica.threadpool;

import java.util.ArrayList;

/**
 *
 */
public class BlockingThreadList {
    private static ArrayList<RequestExecutor> requestExecutors;
    private static Integer coreSize;
    private static Integer maxSize;

    /**
     * @param core
     * @param max
     */
    public BlockingThreadList(int core, int max) {
        requestExecutors = new ArrayList<>();
        coreSize = core;
        maxSize = max;
    }

    /**
     * @param thread
     * @param request
     * @return
     */
    public static synchronized Boolean addThreadToFactory(RequestExecutor thread, Request request) {
        if (requestExecutors.size() >= coreSize && requestExecutors.size() < maxSize) {
            requestExecutors.add(thread);
            request.setNameThread(thread.getNameThread());
            thread.setRequest(request);
            thread.start();
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public static Boolean isFullThread() {
        return requestExecutors.size() == maxSize;
    }

    public static void reset() {
        int count = 0;
        int size = requestExecutors.size() - coreSize;
        boolean flag = true;
        while (flag) {
            for (int index = 0; index < requestExecutors.size(); index++) {
                if (count == size) {
                    flag = false;
                    break;
                }
                RequestExecutor requestExecutor = requestExecutors.get(index);
                if (Constant.STATUS_WAITING.equals(requestExecutor.getState().toString())) {
                    requestExecutor.interrupt();
                    requestExecutors.remove(index);
                    count++;
                }
            }
        }
        System.out.println("Size: " + requestExecutors.size());
    }

    /**
     * Getter for property 'requestExecutors'.
     *
     * @return Value for property 'requestExecutors'.
     */
    public static ArrayList<RequestExecutor> getRequestExecutors() {
        return requestExecutors;
    }

    /**
     * Setter for property 'requestExecutors'.
     *
     * @param requestExecutors Value to set for property 'requestExecutors'.
     */
    public static void setRequestExecutors(ArrayList<RequestExecutor> requestExecutors) {
        BlockingThreadList.requestExecutors = requestExecutors;
    }
}