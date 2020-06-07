package server.classes;

import server.Protocol.Command;
import static server.Protocol.Command.*;

import java.util.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Worker implements Runnable {

    private DataInputStream in;
    private DataOutputStream out;
    private boolean connected = false;
    private Socket connection;

    Queue<Command> sendQueue = new LinkedList<>();

    private UUID uuid = null;
    private String username = null;
    private String ipaddress = null;

    public Worker(Socket connection) {
        try {
            connection = connection;

            in = new DataInputStream(connection.getInputStream());
            out = new DataOutputStream(connection.getOutputStream());

            out.writeUTF(new Command(DETAILS).toString());
            this.ipaddress = connection.getLocalAddress().getHostAddress();

            Command command = Command.valueOf(in.readUTF());

            if (command.command.equals(DETAILS)) {
                this.username = command.getParam("username");
                this.uuid = UUID.fromString(command.getParam("uuid"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (connected) {
                // check the socket fo incomming data
                while (in.available() > 0) {
                    var command = Command.valueOf(in.readUTF());

                    switch (command.command) {

                    }
                }

                // check the send queue for outgoing data.
                while (!sendQueue.isEmpty()) {
                    var command = sendQueue.remove();

                    switch (command.command) {

                        // todo look to see if this can be cleaned up
                        // update the clients contacts
                        case UPDATE_CLIENTS:
                            synchronized (this) {
                                out.writeUTF(command.toString());

                                // see if the client responds
                                if (valueOf(in.readUTF()).command.equals(SUCCESS)) {
                                    var nextPart = sendQueue.remove();
                                    while (nextPart.command.equals(CLIENT)) {
                                        out.writeUTF(nextPart.toString());

                                        // if received continue normally
                                        if (valueOf(in.readUTF()).command.equals(SUCCESS)) {
                                            nextPart = sendQueue.remove();

                                        // otherwise send error and remove all client connections
                                        } else {
                                            out.writeUTF(ERROR);
                                            var removing = true;
                                            while (removing) {
                                                Command peak = sendQueue.peek();
                                                if (peak.command.equals(CLIENT)) {
                                                    sendQueue.remove();
                                                } else if (peak.command.equals(SUCCESS)) {
                                                    sendQueue.remove();
                                                    removing = false;
                                                }
                                            }
                                        }
                                        nextPart = sendQueue.remove();
                                    }
                                } else {
                                    out.writeUTF(ERROR);
                                }
                            }

                        // tell the client to disconnect
                        case DISCONNECT:
                            synchronized (this) {

                            }

                        default:
                            System.out.println("unknown command");
                            System.out.println("command = " + command);

                    }
                }
            }
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.connected = false;
        }
    }

    public synchronized void updateClientList(ArrayList<Worker> clientList) {
        sendQueue.add(new Command(UPDATE_CLIENTS));

        for (Worker i : clientList) {
            sendQueue.add(Command.valueOf(i.toString()));
        }

        sendQueue.add(new Command(SUCCESS));
    }

    public synchronized void disconnect() throws IOException {

        this.connected = false;
    }

    public String getUUID() {
        return this.uuid.toString();
    }

    @Override
    public String toString() {
        return "!Client:" +
                " uuid:" + uuid +
                " username:" + username +
                " ipaddress:" + ipaddress;
    }
}
