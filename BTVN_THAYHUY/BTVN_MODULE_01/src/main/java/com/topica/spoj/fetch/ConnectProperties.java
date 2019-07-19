package com.topica.spoj.fetch;

public class ConnectProperties {
    private String storeType;
    private String nameStore;
    private String hostname;
    private String port;
    private String username;
    private String password;

    public ConnectProperties() {
    }

    public ConnectProperties(String storeType, String nameStore, String hostname, String port, String username, String password) {
        this.storeType = storeType;
        this.nameStore = nameStore;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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
}
