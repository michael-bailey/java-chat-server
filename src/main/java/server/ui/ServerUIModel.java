package server.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import server.classes.JavaServer;
import server.classes.Worker;
import server.delegates.IJavaServerDelegate;

import java.io.IOException;
import java.util.ArrayList;

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
        this.statusText.set("Status: Runnning");
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

    }

    @Override
    public void clientWillDisconnect() {

    }

    @Override
    public void clientDidDisconnect() {

    }
}
