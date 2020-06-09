package server.delegates;

import server.classes.Worker;

import java.util.ArrayList;

public interface IJavaServerDelegate {
    void serverWillStart();
    void serverDidStart();

    void serverWillStop();
    void serverDidStop();

    void clientWillConnect();
    void clientDidConnect();

    void clientWillDisconnect();
    void clientDidDisconnect();
}
