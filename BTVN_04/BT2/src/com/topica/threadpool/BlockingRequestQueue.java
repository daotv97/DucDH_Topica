package com.topica.threadpool;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingRequestQueue {
    private static final Integer EMPTY = 0;
    private static Queue<Runnable> queue;
    private static Integer maxSizeQueue;

    public BlockingRequestQueue(int size) {
        maxSizeQueue = size;
        queue = new LinkedList<Runnable>();
    }

    public static synchronized boolean enqueue(Runnable request) throws InterruptedException {
        if (queue.size() < maxSizeQueue) {
            queue.offer(request);
            return true;
        }
        return false;
    }

    public static Boolean isFullQueue() {
        return queue.size() == maxSizeQueue;
    }

    public static Boolean isEmptyQueue() {
        return queue.isEmpty();
    }

    public static synchronized Runnable dequeue() {
        if (queue.size() > EMPTY) {
            return queue.poll();
        }
        return null;
    }

    public static Queue<Runnable> getQueue() {
        return queue;
    }

    public static void setQueue(Queue<Runnable> queue) {
        BlockingRequestQueue.queue = queue;
    }

}
