package com.topica.threadpool;

/**
 *
 */
public class RequestExecutor extends Thread {
    private Request request;
    private String nameThread;

    /**
     * Constructor for class RequestExecutor
     *
     * @param nameThread
     */
    public RequestExecutor(String nameThread) {
        this.nameThread = nameThread;
    }

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     */
    @Override
    public void run() {
        synchronized (this) {
            while (!this.isInterrupted()) try {
                if (request == null) {
                    this.wait();
                } else {
                    this.request.run();
                    this.request = null;
                }
            } catch (InterruptedException e) {
                System.out.println(this.getNameThread() + " interrupted!");
            }
        }
    }

    /**
     * Getter for property 'nameThread'.
     *
     * @return Value for property 'nameThread'.
     */
    public String getNameThread() {
        return this.nameThread;
    }

    /**
     * Setter for property 'request'.
     *
     * @param request Value to set for property 'request'.
     */
    public void setRequest(Request request) {
        this.request = request;
    }
}