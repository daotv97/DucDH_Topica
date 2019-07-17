package com.topica.repository.impl;

import com.topica.bean.User;
import com.topica.repository.CrudRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String sql = "SELECT * FROM user";
        Set<User> users = new HashSet<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = dataTransferObject(resultSet);
                users.add(user);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM user as `us` WHERE `us`.id = ?";
        User user = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = dataTransferObject(resultSet);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void delete() {
        String sql = "DELETE FROM user WHERE id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, 1);
            preparedStatement.executeUpdate();
            connection.commit();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, 2);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setSavepoint();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, 3);
            preparedStatement.executeUpdate();
            connection.rollback();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public User save(User entity) {
        String sql = "INSERT INTO user(id, username, password, age, email, status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setInt(4, entity.getAge());
            preparedStatement.setString(5, entity.getEmail());
            preparedStatement.setBoolean(6, entity.isStatus());
            preparedStatement.executeUpdate();
            connection.rollback();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return entity;
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
