package vn.edu.topica.beans;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private int age;
    private int point;
    private boolean status;
    private Role role;

    public User() {
    }

    public User(Long id, String username, String password, int age, int point, boolean status, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.point = point;
        this.status = status;
        this.role = role;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", point=" + point +
                ", status=" + status +
                ", role=" + role +
                '}';
    }
}
