package server.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import server.classes.Worker;

import java.io.IOException;
import java.net.URL;

public class WorkerListCell extends ListCell<Worker> {

    @FXML public Label usernameLabel;
    @FXML public Label uuidLabel;

    @Override
    protected void updateItem(Worker item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
            this.setText("Error NullPointer");
            this.setGraphic(null);
        }
    }
}
