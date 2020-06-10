package io.github.michael_bailey.java_server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import io.github.michael_bailey.java_server.ui.ServerUIController;

import java.io.IOException;
import java.net.URL;

public class ServerUIApplication extends Application {

    URL fxml = getClass().getClassLoader().getResource("server/ui/serverUI.fxml");

    ServerUIController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setMinWidth(200);
        stage.setMinHeight(300);

        FXMLLoader fxmlLoader = new FXMLLoader(fxml);

        stage.setScene(fxmlLoader.load());
        stage.show();

        this.controller = fxmlLoader.getController();
    }
}
