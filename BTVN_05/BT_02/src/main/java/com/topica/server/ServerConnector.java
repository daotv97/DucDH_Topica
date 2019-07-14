package com.topica.server;

import com.topica.utils.UserAccount;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerConnector {
    private final Integer port;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
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
            close();
        }
    }

    private void openServer() throws IOException {
        server = new ServerSocket(port);
        onLog("Server is running...");
    }

    private void listening() throws IOException {
        socket = server.accept();
        onLog("Waiting client connect...");
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        authenticate();
        System.out.println("A new client connected");
    }

    private void authenticate() throws IOException {
        boolean isExistUser = false;
        String[] account = dataInputStream.readUTF().split("\\:");
        if (userAccountList.stream().anyMatch(userAccount -> userAccount.getUsername().equals(account[0]) && userAccount.getPassword().equals(account[1])))
            isExistUser = true;

        if (isExistUser)
            dataOutputStream.writeBoolean(true);
        else
            dataOutputStream.writeBoolean(false);
    }

    private void initUser() {
        userAccountList.add(new UserAccount("user1", "12345"));
        userAccountList.add(new UserAccount("user2", "12345"));
        userAccountList.add(new UserAccount("user3", "12345"));
        userAccountList.add(new UserAccount("user4", "12345"));
        userAccountList.add(new UserAccount("user5", "12345"));
    }

    private void close() throws IOException {
        if (socket != null)
            socket.close();
        if (dataInputStream != null)
            dataInputStream.close();
        if (dataOutputStream != null)
            dataOutputStream.close();
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }
}
