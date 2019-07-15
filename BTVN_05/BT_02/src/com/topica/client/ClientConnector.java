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
    private boolean isLogged;
    private Socket loginSocket;
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
        listening();
        close();
    }

    public void terminate() throws IOException {
        stopped = true;
        onLog("... [Client] disconnecting...");
        dataOutputStream.writeInt(0);
    }

    private void listening() throws IOException {
        int response = 1;
        dataOutputStream = new DataOutputStream(loginSocket.getOutputStream());
        dataInputStream = new DataInputStream(loginSocket.getInputStream());
        while (response > 0 && !stopped) {

            onLog("++++++ Send data to server: " + response);
            dataOutputStream.writeInt(response);

            response = dataInputStream.readInt();
            onLog("------ Response from server: " + response);
        }
        onLog("===> [Client] disconnected. <===");
    }

    private void login() throws IOException {
        if (stopped) return;
        if (loginSocket != null && loginSocket.isConnected()) throw new AlreadyConnectedException();
        loginSocket = new Socket(hostname, port);
        onLog("[+] [Client] Connected to " + loginSocket.getRemoteSocketAddress());

        while (!isLogged) {
            dataOutputStream = new DataOutputStream(loginSocket.getOutputStream());
            dataInputStream = new DataInputStream(loginSocket.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            onLog("Username: ");
            stringBuilder.append(scanner.nextLine());

            onLog("Password: ");
            stringBuilder.append(":" + scanner.nextLine());

            dataOutputStream.writeUTF(stringBuilder.toString());
            isLogged = dataInputStream.readBoolean();
            if (!isLogged) {
                onLog("Account authentication failed!");
            } else {
                onLog("Connecting to server...");
            }
        }
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
