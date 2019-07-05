package com.topica.threadpool.database;

public class DataSource {
    private static ConnectionPool pool = new ConnectionPool();

    public static Connection getConnection() throws ClassNotFoundException {
        Connection connection = pool.getConnectionFromPool();
        return connection;
    }

    public static void returnConnection(Connection connection) {
        pool.returnConnectionToPool(connection);
    }
}
