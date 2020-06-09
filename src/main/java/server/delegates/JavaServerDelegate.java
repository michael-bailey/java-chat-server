package server.delegates;

import server.classes.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaServerDelegate implements IJavaServerDelegate {
    @Override
    public void serverWillStart() {
        System.out.println("server is starting");
    }

    @Override
    public void serverDidStart() {
        System.out.println("server started");
    }

    @Override
    public void serverWillStop() {
        System.out.println("server is stopping");
    }

    @Override
    public void serverDidStop() {
        System.out.println("server has stopped");
    }

    @Override
    public void clientWillConnect() {
        System.out.println("client is connecting");
    }

    @Override
    public void clientDidConnect() {
        System.out.println("client has connected");
    }

    @Override
    public void clientWillDisconnect() {
        System.out.println("client is disconnecting");
    }

    @Override
    public void clientDidDisconnect() {
        System.out.println("client has disconnected");
    }
}
