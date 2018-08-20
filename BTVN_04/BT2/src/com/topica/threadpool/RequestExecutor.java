package com.topica.threadpool;

public class FactoryThread extends Thread {
    private RequestHandler requestHandler;
    private String nameThread;

    public FactoryThread(String nameThread) {
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
