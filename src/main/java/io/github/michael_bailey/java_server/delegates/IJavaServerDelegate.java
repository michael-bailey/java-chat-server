package io.github.michael_bailey.java_server.delegates;

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
