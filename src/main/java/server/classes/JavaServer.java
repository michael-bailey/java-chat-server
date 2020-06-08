package server.classes;

import server.Protocol.Command;
import server.delegates.IJavaServerDelegate;
import server.delegates.JavaServerDelegate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static server.Protocol.Command.*;

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
        try {
            srvSock = new ServerSocket(6000);
            thread = new Thread(() -> run());
            connectionThreadPool = Executors.newCachedThreadPool();


            thread.start();
            running = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean stop() {
        synchronized (this.running) {
            try {
                this.srvSock.close();
                this.connectionThreadPool.shutdownNow();
                this.thread.interrupt();

                this.srvSock = null;
                this.connectionThreadPool = null;
                this.thread = null;
                this.running = false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    public void run()  {
        while (running) {

            try {
                var connection = srvSock.accept();
                DataInputStream in = new DataInputStream(connection.getInputStream());
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                // decide if the connection will be kept or discarded
                out.writeUTF(new Command(REQUEST).toString());
                out.flush();

                Command response = Command.valueOf(in.readUTF());

                switch (response.command) {
                    case INFO:
                        var info = new HashMap<String, String>();
                        info.put("name", this.name);
                        info.put("owner", this.owner);
                        out.writeUTF(new Command(SUCCESS, info).toString());
                        out.flush();
                        connection.close();
                        break;

                    case CONNECT:
                        Worker worker = new Worker(connection);
                        this.clientList.add(worker);
                        connectionThreadPool.execute(worker);

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

    public boolean isRunning() {
        return running;
    }
}
