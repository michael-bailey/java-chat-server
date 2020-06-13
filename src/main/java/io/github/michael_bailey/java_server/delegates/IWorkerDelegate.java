package io.github.michael_bailey.java_server.delegates;

import io.github.michael_bailey.java_server.classes.Worker;

import java.util.HashMap;

public interface IWorkerDelegate {
    default void requestUpdateClients(Worker sender) { }

    default void clientWillConnect(Worker sender) { }
    default void clientDidConnect(Worker Sender) { }

    default void clientWillDisconnect(Worker sender) { }
    default void clientDidDisconnect(Worker sender) { }

    default void clientWillRequestInfo(Worker worker) { }

    default void clientDidRequestInfo(Worker worker) { }
}
