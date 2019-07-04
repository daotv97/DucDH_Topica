package com.topica.threadpool;


/**
 *
 */
public class Request implements Runnable {
    private String nameRequest;
    private String nameThread;

    /**
     * @param nameRequest
     */
    public Request(String nameRequest) {
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
            System.out.println("Start name request : " +
                    this.nameRequest);

            Thread.sleep(Constant.TIME_RUNNING);

            System.out.println("Finish name request : " +
                    this.nameRequest +
                    "--" +
                    "Name thread :" +
                    this.nameThread +
                    "--" +
                    " Size array thread :" +
                    BlockingThreadList.getRequestExecutors().size() +
                    "--" +
                    " Size queue :" +
                    BlockingRequestQueue.getQueue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Getter for property 'nameRequest'.
     *
     * @return Value for property 'nameRequest'.
     */
    public String getNameRequest() {
        return this.nameRequest;
    }

    /**
     * Setter for property 'nameRequest'.
     *
     * @param nameRequest Value to set for property 'nameRequest'.
     */
    public void setNameRequest(String nameRequest) {
        this.nameRequest = nameRequest;
    }

    /**
     * Getter for property 'nameThread'.
     *
     * @return Value for property 'nameThread'.
     */
    public String getNameThread() {
        return nameThread;
    }

    /**
     * Setter for property 'nameThread'.
     *
     * @param nameThread Value to set for property 'nameThread'.
     */
    public void setNameThread(String nameThread) {
        this.nameThread = nameThread;
    }
}