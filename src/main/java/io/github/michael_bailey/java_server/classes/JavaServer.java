package io.github.michael_bailey.java_server.classes;

import io.github.michael_bailey.java_server.Protocol.Command;
import io.github.michael_bailey.java_server.delegates.IJavaServerDelegate;
import io.github.michael_bailey.java_server.delegates.IWorkerDelegate;
import io.github.michael_bailey.java_server.delegates.JavaServerDelegate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.michael_bailey.java_server.Protocol.Command.*;

public class JavaServer implements IWorkerDelegate {

    private IJavaServerDelegate delegate;

    String name = "testserver";
    String owner = "michael-bailey";

    private final HashMap<UUID, Worker> clientMap = new HashMap<>();


    private ServerSocket srvSock;
    private ExecutorService connectionThreadPool;
    private ExecutorService tmpConnectionPool;

    private Boolean running = false;

    private Thread thread;

    public JavaServer() throws IOException {
        this.delegate = new JavaServerDelegate();
    }

    public JavaServer(IJavaServerDelegate delegate) throws IOException {
        this.delegate = delegate;
    }

    public boolean start() {
        delegate.serverWillStart();
        try {
            srvSock = new ServerSocket(6000);
            thread = new Thread(() -> run());
            connectionThreadPool = Executors.newCachedThreadPool();
            tmpConnectionPool = Executors.newFixedThreadPool(16);

            thread.start();
            running = true;
            delegate.serverDidStart();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean stop() {
        delegate.serverWillStop();
        synchronized (this.running) {
            try {
                this.srvSock.close();
                this.connectionThreadPool.shutdownNow();
                this.tmpConnectionPool.shutdownNow();
                this.thread.interrupt();

                this.srvSock = null;
                this.connectionThreadPool = null;
                this.tmpConnectionPool = null;
                this.thread = null;
                this.running = false;

                delegate.serverDidStop();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    public void run()  {
        while (running) {
            try {
                var connection = srvSock.accept();
                connectionThreadPool.execute(() -> new Worker(connection, this));
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    System.out.println("socket closed");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateAllClients() {
        this.clientMap.forEach((key, value) -> {
            value.updateClientList(this.getWorkers());
        });
    }

    public Worker[] getWorkers() {
        var workers = new Worker[this.clientMap.size()];
        this.clientMap.values().toArray(workers);
        return workers;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void clientDidConnect(Worker sender) {
        System.out.println("server: Client Connected");

        delegate.clientWillConnect();
        this.clientMap.put(sender.getUUID(), sender);
        this.connectionThreadPool.execute(sender);

        var clientCommand = valueOf(sender.toString());
        this.clientMap.forEach((key, value) -> {
            value.sendQueue.add(clientCommand);
        });

        delegate.clientDidConnect();
    }

    @Override
    public void clientDidDisconnect(Worker sender) {
        System.out.println("server: Client disconnected");
        delegate.clientWillDisconnect();
        this.clientMap.remove(sender.getUUID());

        var a = new HashMap<String, String>();
        a.put("uuid", sender.getUUID().toString());
        var clientCommand = new Command(CLIENT_REMOVE, a);
        this.clientMap.forEach((key, value) -> {
            value.sendQueue.add(clientCommand);
        });

        delegate.clientDidDisconnect();
    }

    @Override
    public void clientWillRequestInfo(Worker sender) {
        var params = new HashMap<String, String>();
        params.put("owner", this.owner);
        params.put("name", this.name);
        try {
            sender.out.writeUTF(new Command(SUCCESS, params).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void requestUpdateClients(Worker sender) {
        this.clientMap.forEach((key, value) -> {
            sender.sendQueue.add(valueOf(value.toString()));
        });
    }
}
