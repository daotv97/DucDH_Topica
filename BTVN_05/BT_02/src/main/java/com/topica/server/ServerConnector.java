package com.topica.server;

import com.topica.utils.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerConnector implements ServerService {
    private final Integer port;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
    private Set<User> userList = null;

    public ServerConnector(Integer port) {
        this.port = port;
        userList = new HashSet<User>(5);
        initUser();
    }

    public void openServer() throws IOException {
        server = new ServerSocket(port);
        System.out.println("Server is running...");
    }

    @Override
    public void listening() throws IOException {
        socket = server.accept();
        System.out.println("Connecting...");
        authenticateClient();
        System.out.println("A new client connected");
    }

    private void authenticateClient() throws IOException {
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        boolean isExistUser = false;
        while (!isExistUser) {
            String[] account = dataInputStream.readUTF().split("\\:");
            if (userList.stream().anyMatch(user -> user.getUsername().equals(account[0]) && user.getPassword().equals(account[1])))
                isExistUser = true;

            if (isExistUser)
                dataOutputStream.writeBoolean(true);
            else
                dataOutputStream.writeBoolean(false);
        }
    }

    private void receiveDataFromClient() throws IOException {
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    private void sendDataToClient() throws IOException {

    }

    private void initUser() {
        userList.add(new User("user1", "12345"));
        userList.add(new User("user2", "12345"));
        userList.add(new User("user3", "12345"));
        userList.add(new User("user4", "12345"));
        userList.add(new User("user5", "12345"));
    }

    @Override
    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (dataInputStream != null)
            dataInputStream.close();
        if (dataOutputStream != null)
            dataOutputStream.close();
    }
}
