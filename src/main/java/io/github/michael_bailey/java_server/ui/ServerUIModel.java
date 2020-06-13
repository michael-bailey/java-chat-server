package io.github.michael_bailey.java_server.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import io.github.michael_bailey.java_server.classes.JavaServer;
import io.github.michael_bailey.java_server.classes.Worker;
import io.github.michael_bailey.java_server.delegates.IJavaServerDelegate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;

public class ServerUIModel implements IJavaServerDelegate {

    private JavaServer server;

    public SimpleListProperty<Worker> workers = new SimpleListProperty(FXCollections.observableList( new ArrayList<Worker>()));
    public SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    public SimpleStringProperty statusText = new SimpleStringProperty("");

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
    public void clientDidConnect() {
        System.out.println("server model: a client connected");
        System.out.println("this.server.getWorkers() = " + this.server.getWorkers());
        Platform.runLater(() -> this.workers.setAll(this.server.getWorkers()));
    }

    @Override
    public void clientDidDisconnect() {
        System.out.println("server model: a client disconnected");
        System.out.println("this.server.getWorkers() = " + this.server.getWorkers());
        Platform.runLater(() -> this.workers.setAll(this.server.getWorkers()));
    }
}
