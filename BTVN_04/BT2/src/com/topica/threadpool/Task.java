package com.topica.threadpool;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {
    private static final Long TIME_SLEEP = 1000L;
    private String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
        System.out.println("Start task: " + name + "-- time: " + Calendar.getInstance().getTime());
        try {
            Thread.sleep(TIME_SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish task: " + name + "-- time: " + Calendar.getInstance().getTime());
    }
}
