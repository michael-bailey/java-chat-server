package io.github.michael_bailey.java_server.delegates;

public interface IJavaServerDelegate {
    default void serverWillStart() {}
    default void serverDidStart() {}

    default void serverWillStop() {}
    default void serverDidStop() {}

    default void clientWillConnect() {}
    default void clientDidConnect() {}

    default void clientWillDisconnect() {}
    default void clientDidDisconnect() {}
}
