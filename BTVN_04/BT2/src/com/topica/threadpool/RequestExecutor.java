package com.topica.threadpool;

public class RequestExecutor extends Thread {
    private RequestHandler requestHandler = null;
    private String nameThread;

    public RequestExecutor(String nameThread) {
        this.nameThread = nameThread;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    if (requestHandler != null) {
                        this.requestHandler.run();
                        this.requestHandler = null;
                    } else {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getNameThread() {
        return this.nameThread;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
}
