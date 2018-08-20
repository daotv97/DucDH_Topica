package com.topica.threadpool;


public class RequestHandler extends Thread implements Runnable {

    private static final Integer TIME_SLEEP = 6000;
    private String nameRequest;
    private String nameThread;

    public RequestHandler(String nameRequest) {
        this.nameRequest = nameRequest;
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
        try {

            // Processing Time
            Thread.sleep(TIME_SLEEP);
            System.out.println("Name request :"
                    + this.nameRequest
                    + "--"
                    + "Name thread :"
                    + this.nameThread
                    + "--"
                    + " Size Array thread :"
                    + BlockingThreadList.getFactoryThreads().size()
                    + "--"
                    + " Size queue :"
                    + BlockingRequestQueue.getQueue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getNameRequest() {
        return this.nameRequest;
    }

    public void setNameThread(String nameThread) {
        this.nameThread = nameThread;
    }
}
