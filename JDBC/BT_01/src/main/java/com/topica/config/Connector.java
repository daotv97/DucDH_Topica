package com.topica.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
    private static final String FILE_NAME = "database.properties";

    public static Connection getConnect() throws SQLException, IOException, ClassNotFoundException {
        InputStream input = Connector.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Properties properties = new Properties();
        if (input == null) {
            throw new FileNotFoundException("Sorry! unable to find...");
        }
        properties.load(input);
        String driver = properties.getProperty("database.driver");
        String username = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        String url = properties.getProperty("database.url");

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}
