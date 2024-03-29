package com.topica.server;

import com.topica.server.pool.ConnectionPoolExecutor;
import com.topica.utils.Constant;
import com.topica.utils.UserAccount;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerConnector {
    private final Integer port;
    private ServerSocket server;
    private Socket socket;
    private Set<UserAccount> userAccountList;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private BlockingQueue blockingQueue;
    private ConnectionPoolExecutor connectionPoolExecutor;

    public ServerConnector(Integer port, Set<UserAccount> userAccountList) {
        this.port = port;
        this.userAccountList = userAccountList;
        blockingQueue = new ArrayBlockingQueue(Constant.CAPACITY);
        connectionPoolExecutor = new ConnectionPoolExecutor(Constant.CORE_POOL_SIZE, Constant.MAXIMUM_POOL_SIZE, blockingQueue);
    }

    public void start() throws IOException {
        openServer();
        while (true) {
            listening();
        }
    }

    private void openServer() throws IOException {
        server = new ServerSocket(port);
        System.out.println("Server is running...");
    }

    private void listening() throws IOException {
        socket = server.accept();
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());

        if (dataInputStream.readUTF().equals("login")) {
            ConnectionThread connectionThread = new ConnectionThread(socket, userAccountList, dataInputStream, dataOutputStream);
            connectionThread.start();
        }
    }

    private class ConnectionThread extends Thread {
        private Socket socket;
        private String username;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private Set<UserAccount> userAccountList;

        public ConnectionThread(Socket socket
                , Set<UserAccount> userAccountList
                , DataInputStream dataInputStream
                , DataOutputStream dataOutputStream) {
            this.socket = socket;
            this.userAccountList = userAccountList;
            this.dataInputStream = dataInputStream;
            this.dataOutputStream = dataOutputStream;
        }

        @Override
        public void run() {
            try {
                if (!connectionPoolExecutor.isFullQueue() && !connectionPoolExecutor.isFullThreadPool()) {
                    dataOutputStream.writeBoolean(false);
                    authenticate();
                    Connection connection = new Connection(socket, username);
                    connectionPoolExecutor.execute(connection);
                } else {
                    dataOutputStream.writeBoolean(true);
                    onLog("Server is full!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void authenticate() throws IOException {
            boolean isUserExist = false;

            while (!isUserExist) {
                String[] account = dataInputStream.readUTF().split("\\:");
                if (userAccountList.stream().anyMatch(userAccount -> userAccount
                        .getUsername()
                        .equals(account[0]) && userAccount
                        .getPassword().equals(account[1])))
                    isUserExist = true;

                if (isUserExist) {
                    onLog("[Client {" + account[0] + "}]: connected.");
                    this.username = account[0];
                    dataOutputStream.writeBoolean(true);
                } else dataOutputStream.writeBoolean(false);
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
}