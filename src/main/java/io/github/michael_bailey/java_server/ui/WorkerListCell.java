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

        this.setText(null);
        this.setGraphic(null);

        if (item == null) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            this.usernameLabel.textProperty().setValue(item.getUsername());
            this.uuidLabel.textProperty().setValue(item.getUUID().toString());
            this.setGraphic(root);
        }
    }
}
