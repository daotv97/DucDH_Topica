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
    private Socket socket;
    private ServerSocket server;
    private Set<UserAccount> userAccountList;
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
        onLog("Server is running...");
    }

    private void listening() throws IOException {
        socket = server.accept();
        Authentication authentication = new Authentication(socket, userAccountList);
        authentication.start();
    }


    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }

    private class Authentication extends Thread {
        private Socket socket;
        private String username;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private Set<UserAccount> userAccountList;

        public Authentication(Socket socket, Set<UserAccount> userAccountList) {
            this.socket = socket;
            this.userAccountList = userAccountList;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                authenticate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void authenticate() throws IOException {
            boolean isUserExist = false;

            while (!isUserExist) {
                String[] account = dataInputStream.readUTF().split("\\:");
                if (userAccountList.stream().anyMatch(userAccount -> userAccount.getUsername().equals(account[0]) && userAccount.getPassword().equals(account[1])))
                    isUserExist = true;

                if (isUserExist) {
                    onLog("[Client {" + account[0] + "}]: connected.");
                    username = account[0];
                    dataOutputStream.writeBoolean(true);
                } else
                    dataOutputStream.writeBoolean(false);
            }
        }

        private void close() throws IOException {
            if (socket != null)
                socket.close();
            if (dataOutputStream != null)
                dataOutputStream.close();
            if (dataInputStream != null)
                dataInputStream.close();
        }

        public void onLog(String message) {
            System.out.println(message);
        }

        public void onLogError(String message) {
            System.err.println(message);
        }

    }

}
