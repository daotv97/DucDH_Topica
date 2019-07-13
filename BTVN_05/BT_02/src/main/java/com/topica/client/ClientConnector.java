package com.topica.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnector implements ClientService {
    private final String hostName;
    private final Integer port;
    private Boolean isConnected = false;
    private Socket socket = null;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;
    private Scanner scanner;

    public ClientConnector(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
        scanner = new Scanner(System.in);
    }

    @Override
    public void createConnect() throws IOException {
        socket = new Socket(hostName, port);
        authenticate();
    }

    @Override
    public void connection() throws IOException {
        sendDataToServer();
    }

    private void authenticate() throws IOException {
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            StringBuilder stringBuilder = new StringBuilder();

            System.out.println("====== Authentication ======");
            System.out.print("Username: ");

            stringBuilder.append(scanner.nextLine());
            System.out.print("Password: ");
            stringBuilder.append(":" + scanner.nextLine());
            dataOutputStream.writeUTF(stringBuilder.toString());
            isAuthenticated = dataInputStream.readBoolean();
        }
    }

    private void sendDataToServer() throws IOException {
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

    }

    private void receiveDataFromServer() {

    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (dataOutputStream != null) {
            dataOutputStream.close();
        }
        if (dataInputStream != null) {
            dataInputStream.close();
        }
    }
}
