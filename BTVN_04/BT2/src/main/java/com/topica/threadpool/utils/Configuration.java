package com.topica.threadpool.utils;

public class Configuration {
    private static Configuration configuration = new Configuration();
    public String DB_USER_NAME;
    public String DB_PASSWORD;
    public String DB_URL;
    public String DB_DRIVER;
    public Integer DB_MAX_CONNECTIONS;

    public Configuration() {
        init();
    }

    public static Configuration getInstance() {
        return configuration;
    }

    private void init() {
        DB_USER_NAME = "root";
        DB_PASSWORD = "root";
        DB_URL = "jdbc:mysql://localhost:3306/topica";
        DB_DRIVER = "com.mysql.jdbc.Driver";
        DB_MAX_CONNECTIONS = 5;
    }
}
