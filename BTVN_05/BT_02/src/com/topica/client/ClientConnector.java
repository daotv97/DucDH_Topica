package com.topica.client;

import java.io.*;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;
import java.util.Scanner;

public class ClientConnector {
    private final String hostname;
    private final Integer port;
    private boolean stopped;
    private boolean isLogged;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Scanner scanner;

    public ClientConnector(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        stopped = false;
        login();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processing();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    public void terminate() throws IOException {
        stopped = true;
        onLog("... [Client] disconnecting...");
        dataOutputStream.writeInt(0);
    }

    private void processing() throws IOException {
        int response = 1;
        while (response > 0 && !stopped) {

            onLog("++++++ Send data to server: " + response);
            dataOutputStream.writeInt(response);

            response = dataInputStream.readInt();
            onLog("------ Response from server: " + response);
        }
        onLog("===> [Client] disconnected. <===");
    }

    private void sendMessage(String msg) {
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            System.err.println("ERROR: error sending data");
        }
    }

    private void login() throws IOException {
        if (stopped) return;
        if (socket != null && socket.isConnected()) throw new AlreadyConnectedException();

        socket = new Socket(hostname, port);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());

        dataOutputStream.writeUTF("login");

        boolean isServerFull = dataInputStream.readBoolean();
        if (isServerFull) {
            onLogError("500 Error!");
        } else {
            onLog("[+] [Client] Connected to " + socket.getRemoteSocketAddress());
            while (!isLogged) {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                onLog("Username: ");
                stringBuilder.append(scanner.nextLine());

                onLog("Password: ");
                stringBuilder.append(":" + scanner.nextLine());

                dataOutputStream.writeUTF(stringBuilder.toString());
                isLogged = dataInputStream.readBoolean();
                onLog(!isLogged ? "Account authentication failed!" : "Connecting to server...");
            }
        }
    }

    private void close() throws IOException {
        if (socket != null) socket.close();
        if (dataOutputStream != null) dataOutputStream.close();
        if (dataInputStream != null) dataInputStream.close();
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }
}
