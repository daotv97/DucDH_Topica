package com.topica.repository.impl;

import com.topica.bean.User;
import com.topica.repository.CrudRepository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepository implements CrudRepository<User, Long> {

    private Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Set<User> findAll() {
        String query = "{CALL findAll()}";
        Set<User> users = new HashSet<>();
        CallableStatement callableStatement = null;
        try {
            callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                User user = dataTransferObject(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = "{CALL findById(?)}";
        CallableStatement callableStatement = null;
        User user = null;
        try {
            callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                user = dataTransferObject(resultSet);
            }
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User entity) {

    }

    private User dataTransferObject(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(1));
        user.setUsername(resultSet.getString(2));
        user.setPassword(resultSet.getString(3));
        user.setAge(resultSet.getInt(4));
        user.setEmail(resultSet.getString(5));
        user.setStatus(resultSet.getBoolean(6));
        return user;
    }
}
