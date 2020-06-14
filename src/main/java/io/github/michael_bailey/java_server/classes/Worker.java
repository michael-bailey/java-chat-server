package io.github.michael_bailey.java_server.classes;

import io.github.michael_bailey.java_server.Protocol.Command;
import io.github.michael_bailey.java_server.delegates.IWorkerDelegate;

import static io.github.michael_bailey.java_server.Protocol.Command.*;

import java.util.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Worker implements Runnable {

    private Socket connection;
    public DataInputStream in;
    public DataOutputStream out;

    Queue<Command> sendQueue = new LinkedList<>();

    private UUID uuid = null;
    private String username = null;
    private String ipaddress = null;
    private IWorkerDelegate delegate;

    private boolean connected = false;

    public Worker(Socket connection, IWorkerDelegate delegate) {

        this.delegate = delegate;

        try {
            this.connection = connection;
            in = new DataInputStream(connection.getInputStream());
            out = new DataOutputStream(connection.getOutputStream());

            out.writeUTF(new Command(REQUEST).toString());

            var command = Command.valueOf(in.readUTF());

            switch (command.command) {
                case INFO:
                    delegate.clientWillRequestInfo(this);
                    connection.close();
                    delegate.clientDidRequestInfo(this);
                    break;

                case CONNECT:
                    delegate.clientWillConnect(this);

                    this.username = command.getParam("name");
                    this.uuid = UUID.fromString(command.getParam("uuid"));
                    this.ipaddress = this.connection.getInetAddress().getHostAddress();

                    connected = true;

                    out.writeUTF(new Command(SUCCESS).toString());
                    delegate.clientDidConnect(this);
                    break;

                default:
                    out.writeUTF(new Command(ERROR).toString());
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Worker: started thread");
        try {
            while (connected) {
                // check the socket fo incomming data
                while (in.available() > 0) {
                    System.out.println("incoming data");
                    var command = Command.valueOf(in.readUTF());
                    switch (command.command) {
                        case DISCONNECT:
                            this.disconnect();
                            break;
                    }
                }

                // check the send queue for outgoing data.
                while (!sendQueue.isEmpty()) {
                    var command = sendQueue.remove();

                    switch (command.command) {

                        // todo look to see if this can be cleaned up
                        // update the clients contacts
                        case UPDATE_CLIENTS:
                            System.out.println("updating clients");
                            out.writeUTF(command.toString());
                            if (!valueOf(in.readUTF()).command.equals(SUCCESS)) {
                                sendQueue.clear();
                                out.writeUTF(new Command(ERROR).toString());
                            }
                            break;

                        case CLIENT:
                            System.out.println("worker {"+uuid+"}: sending a client");
                            out.writeUTF(command.toString());
                            if (!valueOf(in.readUTF()).command.equals(SUCCESS)) {
                                sendQueue.clear();
                                out.writeUTF(new Command(ERROR).toString());
                            }
                            break;

                        case SUCCESS:
                            System.out.println("worker {"+uuid+"}: sending success");
                            out.writeUTF(command.toString());
                            if (!valueOf(in.readUTF()).command.equals(SUCCESS)) {
                                sendQueue.clear();
                                out.writeUTF(new Command(ERROR).toString());
                            }
                            break;

                        // tell the client to disconnect
                        case DISCONNECT:
                            System.out.println("worker {"+uuid+"}: sending disconnect");
                            out.writeUTF(new Command(DISCONNECT).toString());
                            this.disconnect();
                            break;

                        default:
                            System.out.println("unknown command");
                            System.out.println("command = " + command);
                            break;

                    }
                }
            }
            System.out.println("Worker: ended thread");
        } catch (IOException e) {
            e.printStackTrace();
            this.connected = false;
        }
    }

    public synchronized void updateClientList(Worker[] clientList) {
        sendQueue.add(new Command(UPDATE_CLIENTS));
        for (Worker i : clientList) {
            sendQueue.add(Command.valueOf(i.toString()));
        }
        sendQueue.add(new Command(SUCCESS));
    }

    public synchronized void disconnect() throws IOException {
        delegate.clientWillDisconnect(this);
        this.connected = false;
        out.writeUTF(SUCCESS);
        connection.close();
        delegate.clientDidDisconnect(this);
    }

    public UUID getUUID() { return this.uuid; }
    public String getUsername() { return this.username; }

    @Override
    public String toString() {
        return "!Client:" +
                " uuid:" + uuid +
                " username:" + username +
                " ipaddress:" + ipaddress;
    }
}
