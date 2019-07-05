package com.topica.threadpool.database;

public class DriverManager {
    private static final String URL = "jdbc:mysql://localhost:3306/topica";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection(String url, String username, String password) {
        if (url.equals(URL) && username.equals(USERNAME) && password.equals(PASSWORD)) {
            return new Connection(2);
        }
        return null;
    }
}
