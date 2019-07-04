package com.topica.threadpool;


public class RequestHandler implements Runnable {
    private String nameRequest;
    private String nameThread;

    public RequestHandler(String nameRequest) {
        this.nameRequest = nameRequest;
    }

    @Override
    public void run() {
        // Processing time
        try {
            System.out.println("Start name request :"
                            + this.nameRequest);

            Thread.sleep(Constant.TIME_RUNNING);

            System.out.println("Finish name request :"
                    + this.nameRequest
                    + "--"
                    + "Name thread :"
                    + this.nameThread
                    + "--"
                    + " Size Array thread :"
                    + BlockingThreadList.getRequestExecutors().size()
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

    public String getNameThread() {
        return nameThread;
    }

    public void setNameThread(String nameThread) {
        this.nameThread = nameThread;
    }
}
