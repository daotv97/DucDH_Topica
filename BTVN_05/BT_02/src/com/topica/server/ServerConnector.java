package com.topica.server;

import com.topica.utils.UserAccount;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerConnector {
    private final Integer port;
    private Socket socket;
    private ServerSocket server;
    private Set<UserAccount> userAccountList;

    public ServerConnector(Integer port) {
        this.port = port;
        userAccountList = new HashSet<>(5);
        initUser();
    }

    public void start() throws IOException {
        openServer();
        while (true) {
            listening();
        }
    }

    private void openServer() throws IOException {
        server = new ServerSocket(port);
        onLog("Server is running...");
    }

    private void listening() throws IOException {
        socket = server.accept();
        Connection connection = new Connection(socket, userAccountList);
        connection.start();
    }

    private void initUser() {
        userAccountList.add(new UserAccount("user1", "12345"));
        userAccountList.add(new UserAccount("user2", "12345"));
        userAccountList.add(new UserAccount("user3", "12345"));
        userAccountList.add(new UserAccount("user4", "12345"));
        userAccountList.add(new UserAccount("user5", "12345"));
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }
}
