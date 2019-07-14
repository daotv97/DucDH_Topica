package com.topica.server;

import com.topica.server.pool.ConnectionPoolExecutor;
import com.topica.utils.Constant;
import com.topica.utils.UserAccount;

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
        Connection connection = new Connection(socket, userAccountList);
        connectionPoolExecutor.execute(connection);
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }
}
