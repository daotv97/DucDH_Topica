package com.topica.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;
import java.util.Scanner;

public class ClientConnector {
    private final String hostname;
    private final Integer port;
    private boolean stopped;
    private Socket loginSocket = null;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;
    private Scanner scanner;

    public ClientConnector(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        login();
        listening();
        close();
    }

    public void terminate() {

    }

    public boolean isConnected() {
        return loginSocket != null && loginSocket.isConnected();
    }

    public boolean isServerReachable() {
        try {
            Socket tempSocket = new Socket(hostname, port);
            tempSocket.isConnected();
            tempSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void listening() throws IOException {

    }

    private void login() throws IOException {
        if (stopped) return;

        onLog("[Client] Connecting...");
        if (loginSocket != null && loginSocket.isConnected()) throw new AlreadyConnectedException();

        loginSocket = new Socket(hostname, port);
        onLog("[Client] Connected to " + loginSocket.getRemoteSocketAddress());

        dataOutputStream = new DataOutputStream(loginSocket.getOutputStream());
        dataInputStream = new DataInputStream(loginSocket.getInputStream());

        StringBuilder stringBuilder = new StringBuilder();

        System.out.println("====== Authentication ======");
        System.out.print("Username: ");

        stringBuilder.append(scanner.nextLine());
        System.out.print("Password: ");
        stringBuilder.append(":" + scanner.nextLine());
        dataOutputStream.writeUTF(stringBuilder.toString());
    }


    private void receiveDataFromServer() {

    }

    private void close() throws IOException {
        if (loginSocket != null) {
            loginSocket.close();
        }
        if (dataOutputStream != null) {
            dataOutputStream.close();
        }
        if (dataInputStream != null) {
            dataInputStream.close();
        }
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }

}
