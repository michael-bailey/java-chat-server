package io.github.michael_bailey.java_server.classes;

import io.github.michael_bailey.java_server.delegates.IJavaServerDelegate;
import io.github.michael_bailey.java_server.delegates.JavaServerDelegate;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaServer {

    private IJavaServerDelegate delegate;

    String name = "testserver";
    String owner = "michael-bailey";

    private final ArrayList<Worker> clientList = new ArrayList();


    private ServerSocket srvSock;
    private ExecutorService connectionThreadPool;
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
                this.thread.interrupt();

                this.srvSock = null;
                this.connectionThreadPool = null;
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
