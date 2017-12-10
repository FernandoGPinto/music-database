
/**
 * Created by Fernando on 18/10/2017.
 */
package test.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class HomeController implements Initializable {

    @FXML private AnchorPane homePane;

    private final List<String> validExtension = Arrays.asList("csv");

    @FXML
    private void handleView() {

        homePane.getChildren().get(1).setVisible(true);
        homePane.getChildren().get(2).setVisible(false);
    }

    @FXML
    private void handleImport() {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Import.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Error", "Please close and try again");
        }

        Scene scene = new Scene(root);

        scene.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && validExtension.containsAll(db.getFiles().stream()
                    .map(file -> FilenameUtils.getExtension(file.getName()))
                    .collect(Collectors.toList()))) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        Stage primaryStage = (Stage) homePane.getScene().getWindow();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void handleHelp() {

        homePane.getChildren().get(1).setVisible(false);
        homePane.getChildren().get(2).setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Node child = null;

        try {
            child = FXMLLoader.load(getClass().getResource("/View.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Error", "Please close and try again");
        }
        homePane.getChildren().add(child);

        //add help child
    }

    private void alert(Alert.AlertType type, String title, String content){

        Alert alert = new Alert(type);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/Style.css").toExternalForm());
        alert.initOwner(homePane.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
