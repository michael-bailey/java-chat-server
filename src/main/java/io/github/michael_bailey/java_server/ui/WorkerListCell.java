package io.github.michael_bailey.java_server.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import io.github.michael_bailey.java_server.classes.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;

public class WorkerListCell extends ListCell<Worker> {

    @FXML public Label usernameLabel;
    @FXML public Label uuidLabel;
    @FXML public AnchorPane root;

    @Override
    protected void updateItem(Worker item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
            this.setText("Error NullPointer");
            this.setGraphic(null);
        }

        this.setGraphic(root);
    }
}
