package io.github.michael_bailey.java_server.classes;

import io.github.michael_bailey.java_server.Protocol.Command;
import io.github.michael_bailey.java_server.delegates.IJavaServerDelegate;
import io.github.michael_bailey.java_server.delegates.JavaServerDelegate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.michael_bailey.java_server.Protocol.Command.*;

public class JavaServer {

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
                tmpConnectionPool.execute(() -> newConnectionHandler(connection));
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    System.out.println("socket closed");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    private void newConnectionHandler(Socket connection) {
        try {
            var in = new DataInputStream(connection.getInputStream());
            var out = new DataOutputStream(connection.getOutputStream());

            out.writeUTF(new Command(REQUEST).toString());

            var command = Command.valueOf(in.readUTF());
            System.out.println("command = " + command.toString());

            switch (command.command) {
                case INFO:
                    var params = new HashMap<String, String>();
                    params.put("owner", this.owner);
                    params.put("name", this.name);
                    System.out.println(new Command(SUCCESS, params).toString());
                    out.writeUTF(new Command(SUCCESS, params).toString());
                    break;

                case CONNECT:
                    delegate.clientWillConnect();
                    var newWorker = new Worker(command.getParam("name"), command.getParam("uuid"), connection);
                    this.clientMap.put(UUID.fromString(command.getParam("uuid")), newWorker);
                    this.connectionThreadPool.execute(newWorker);
                    delegate.clientDidConnect();
                    break;

                default:
                    out.writeUTF(new Command(ERROR).toString());
                    break;
            }
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Worker[] getWorkers() {
       return (Worker[]) this.clientMap.values().toArray();
    }

    public boolean isRunning() {
        return running;
    }
}
