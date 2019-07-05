package com.topica.threadpool.database;

public class Connection implements Runnable {
    private Integer id;

    public Connection(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void run() {
        System.out.printf("Connection..." + id);
    }
}
