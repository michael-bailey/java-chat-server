package io.github.michael_bailey.java_server.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import io.github.michael_bailey.java_server.classes.JavaServer;
import io.github.michael_bailey.java_server.classes.Worker;
import io.github.michael_bailey.java_server.delegates.IJavaServerDelegate;
import javafx.collections.ObservableList;

import java.io.IOException;

public class ServerUIModel implements IJavaServerDelegate {

    public SimpleListProperty<Worker> workers = new SimpleListProperty();
    public SimpleBooleanProperty running = new SimpleBooleanProperty(false);

    public SimpleStringProperty statusText = new SimpleStringProperty();

    private JavaServer server;

    public ServerUIModel() throws IOException {
        server = new JavaServer(this);
        this.statusText.set("Status: not running");
    }

    public void start() {
        this.server.start();
        this.running.set(true);
        this.statusText.set("Status: Running");
    }

    public void stop() {
        this.server.stop();
        this.running.set(false);
        this.statusText.set("Status: not running");
    }

    public boolean isRunning() {
        return running.get();
    }

    public SimpleStringProperty statusTextProperty() {
        return statusText;
    }

    public SimpleListProperty<Worker> workersProperty() {
        return workers;
    }

    @Override
    public void serverWillStart() {

    }

    @Override
    public void serverDidStart() {

    }

    @Override
    public void serverWillStop() {

    }

    @Override
    public void serverDidStop() {

    }

    @Override
    public void clientWillConnect() {

    }

    @Override
    public void clientDidConnect() {
        this.workers.setAll(this.server.getWorkers());
    }

    @Override
    public void clientWillDisconnect() {

    }

    @Override
    public void clientDidDisconnect() {

    }
}
