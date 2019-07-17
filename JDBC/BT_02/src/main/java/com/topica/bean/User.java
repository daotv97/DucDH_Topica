package com.topica.bean;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private int age;
    private String email;
    private boolean status;

    public User() {
    }

    public User(Long id, String username, String password, int age, String email, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
