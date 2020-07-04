package io.github.michael_bailey.java_server.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import io.github.michael_bailey.java_server.classes.Worker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerUIController implements Initializable {

    private final ServerUIModel model = new ServerUIModel();

    private final URL listCellURL = getClass().getClassLoader().getResource("io/github/michael_bailey/java_server/ui/WorkerListCell.fxml");

    @FXML public ListView<Worker> listView;
    @FXML private Label serverStatusLabel;
    @FXML private Button powerButton;
    @FXML private TextArea logView;

    public ServerUIController() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setCellFactory(param -> {
            FXMLLoader loader = new FXMLLoader(listCellURL);
            try {
                loader.load();
                return loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        this.powerButton.setText("Start");
        this.serverStatusLabel.textProperty().bind(model.statusText);
        this.listView.itemsProperty().bind(this.model.workersProperty());
    }

    @FXML
    public void serverToggle(ActionEvent actionEvent) {
        if (model.isRunning()) {
            model.stop();
            this.listView.itemsProperty().get().clear();
            this.powerButton.setText("Start");
        } else {
            model.start();
            this.powerButton.setText("Stop");
        }
    }

    public void stopProgram() {
        this.model.stop();
    }
}
