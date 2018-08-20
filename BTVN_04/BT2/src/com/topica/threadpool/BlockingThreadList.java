package com.topica.threadpool;

import java.util.ArrayList;

public class BlockingThreadList {
    private static ArrayList<FactoryThread> factoryThreads = new ArrayList();
    private static int coreSize;
    private static int maxSize = 5;

    public BlockingThreadList(int core, int max) {
        coreSize = core;
        maxSize = max;
    }

    public static synchronized Boolean addThreadToQueue(FactoryThread thread, RequestHandler requestHandler) throws InterruptedException {
        if (BlockingThreadList.factoryThreads.size() < maxSize) {
            factoryThreads.add(thread);
            factoryThreads.get(factoryThreads.size() - 1).setRequestHandler(requestHandler);

            requestHandler.setNameThread(thread.getNameThread());
            factoryThreads.get(factoryThreads.size() - 1).start();
            return true;
        }
        return false;
    }

    public static ArrayList<FactoryThread> getFactoryThreads() {
        return factoryThreads;
    }

    public static void setFactoryThreads(ArrayList<FactoryThread> factoryThreads) {
        BlockingThreadList.factoryThreads = factoryThreads;
    }
}
