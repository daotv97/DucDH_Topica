package com.topica.threadpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class BlockingRequestQueue {
    private static Queue<Runnable> queue;
    private static Integer maxSizeQueue;

    /**
     * @param size
     */
    public BlockingRequestQueue(int size) {
        maxSizeQueue = size;
        queue = new LinkedList<>();
    }

    /**
     * @param request
     * @return
     */
    public static synchronized Boolean enqueue(Runnable request) {
        if (queue.size() >= maxSizeQueue) {
            return false;
        }
        queue.offer(request);
        return true;
    }

    /**
     * @return
     */
    public static synchronized Runnable dequeue() {
        if (queue.size() <= Constant.EMPTY_QUEUE) {
            return null;
        }
        return queue.poll();
    }

    /**
     * @return
     */
    public static Boolean isFullQueue() {
        return queue.size() == maxSizeQueue;
    }

    /**
     * @return
     */
    public static Boolean isEmptyQueue() {
        return queue.isEmpty();
    }

    /**
     * Getter for property 'queue'.
     *
     * @return Value for property 'queue'.
     */
    public static Queue<Runnable> getQueue() {
        return queue;
    }

    /**
     * Setter for property 'queue'.
     *
     * @param queue Value to set for property 'queue'.
     */
    public static void setQueue(Queue<Runnable> queue) {
        BlockingRequestQueue.queue = queue;
    }

}