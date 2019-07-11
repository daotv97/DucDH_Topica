package com.topica.threadpool.test;

import com.topica.threadpool.utils.Constant;

public class Task implements Runnable {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName() + " Start task: " + name);
        try {
            Thread.sleep(Constant.TIME_EXE_TASK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread: " + Thread.currentThread().getName() + " Finish task: " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
