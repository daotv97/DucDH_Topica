package com.topica.control;

import com.topica.bean.User;
import com.topica.config.Connector;
import com.topica.repository.impl.UserRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Connection connection = Connector.getConnect();
        if (connection != null) {
            UserRepository userRepository = new UserRepository(connection);
            Set<User> users = userRepository.findAll();
            formatTableOutput(users);
        }
    }

    private static void formatTableOutput(Set<User> users) {
        String leftAlignFormat = "| %-4d | %-15s | %-15s | %-4d | %-32s | %-10s |%n";

        System.out.format("+------+-----------------+-----------------+------+----------------------------------+------------+%n");
        System.out.format("| ID   | USERNAME        | PASSWORD        | AGE  | EMAIL                            | STATUS     |%n");
        System.out.format("+------+-----------------+-----------------+------+----------------------------------+------------+%n");
        users.stream().forEach(user -> System.out.format(leftAlignFormat
                , user.getId()
                , user.getUsername()
                , user.getPassword()
                , user.getAge()
                , user.getEmail()
                , user.isStatus()));
        System.out.format("+------+-----------------+-----------------+------+----------------------------------+------------+%n");
    }
}
