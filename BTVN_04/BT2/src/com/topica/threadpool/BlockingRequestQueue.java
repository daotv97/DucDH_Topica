package com.topica.threadpool;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingRequestQueue {
    private static Queue<Runnable> queue;
    private static Integer maxSizeQueue;

    public BlockingRequestQueue(int size) {
        maxSizeQueue = size;
        queue = new LinkedList<>();
    }

    public static synchronized Boolean enqueue(Runnable request) {
        if (queue.size() >= maxSizeQueue) {
            return false;
        }
        queue.offer(request);
        return true;
    }

    public static synchronized Runnable dequeue() {
        if (queue.size() <= Constant.EMPTY_QUEUE) {
            return null;
        }
        return queue.poll();
    }

    public static Boolean isFullQueue() {
        return queue.size() == maxSizeQueue;
    }

    public static Boolean isEmptyQueue() {
        return queue.isEmpty();
    }

    public static Queue<Runnable> getQueue() {
        return queue;
    }

    public static void setQueue(Queue<Runnable> queue) {
        BlockingRequestQueue.queue = queue;
    }

}