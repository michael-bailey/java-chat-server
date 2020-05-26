package server.classes;


import server.delegates.IJavaServerDelegate;
import server.ui.ServerUIModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static server.enums.PROTOCOL_MESSAGES.*;

public class JavaServer {

    private IJavaServerDelegate delegate;


    String name = "testserver";
    String owner = "michael-bailey";

    private final ArrayList<Worker> clientList = new ArrayList();

    private ServerSocket srvSock;
    private ExecutorService threadPool;
    private Boolean running = false;

    private Thread thread;

    public JavaServer() throws IOException {
        thread = new Thread(() -> run());
        threadPool = Executors.newCachedThreadPool();
        srvSock = new ServerSocket(6000);
    }

    public JavaServer(IJavaServerDelegate delegate) throws IOException {
        this.delegate = delegate;
    }

    public boolean start() {
        try {
            srvSock = new ServerSocket(6000);
            thread = new Thread(() -> run());
            threadPool = Executors.newCachedThreadPool();

            thread.start();
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean stop() {
        synchronized (this.running) {
            try {
                this.srvSock.close();
                this.threadPool.shutdownNow();
                this.thread.interrupt();

                this.srvSock = null;
                this.threadPool = null;
                this.thread = null;
                this.running = false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;

    }

    public void run() {
        while (running) {
            try {
                Socket connection = srvSock.accept();

                DataInputStream in = new DataInputStream(connection.getInputStream());
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                // decide if the connection will be kept or discarded
                out.writeUTF("?request:");
                out.flush();

                String response = in.readUTF();

                switch (response) {
                    case INFO:
                        out.writeUTF("!success: name:"+this.name+" owner:"+this.owner+"");
                        out.flush();
                        connection.close();
                        break;

                    case CONNECT:
                        Worker worker = new Worker(connection);
                        this.clientList.add(worker);
                        threadPool.execute(worker);

                        for (Worker i : this.clientList) {
                            i.updateClientList(this.clientList);
                        }
                        break;
                    default:
                        System.out.println("client sent" + response);
                        connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateClientList() {
        delegate.updatedClientList(this.clientList);
    }
}
