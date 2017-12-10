package test.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import test.dao.ProductDAO;
import test.util.ImportRecords;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fernando on 24/10/2017.
 */
public class ImportController implements Initializable {

    @FXML private AnchorPane importPane;
    @FXML private Hyperlink browseLink;

    private static final Pattern pattern = Pattern.compile("(\\w+),(\\w+),(\\d+),(\\d+),(\\d+)");

    @FXML
    public void handleDragDrop(DragEvent event) {

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String filePath = null;
            for (File file:db.getFiles()) {
                filePath = file.getAbsolutePath();
            }
            try {
                verifyContents(filePath);
            } catch (Exception e){
                e.printStackTrace();
                alert(Alert.AlertType.ERROR, "DB Insert Error", "Please check file format and try again");
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    public void handleBrowse() {

        browseLink.setVisited(false);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(importPane.getScene().getWindow());
        if(file != null) {
            try {
                verifyContents(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                alert(Alert.AlertType.ERROR, "DB Insert Error", "Please check file format/contents and try again");
            }
        }
    }

    private void verifyContents(String path) throws Exception {

        List<String> records = null;

        try {
            records = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "File Reading Error", "Please check file and try again");
        }
        Matcher m = pattern.matcher(records.get(0));
        if(m.matches()) {
            //import products
            ImportRecords importRecords = new ImportRecords();
            importRecords.importFile(path);
            alert(Alert.AlertType.CONFIRMATION, "Success", "Import Completed");
        } else {
            ImportRecords importRecords = new ImportRecords();
            ProductDAO productDAO = new ProductDAO();
            //check whether products exist in db
            for(String record : records) {
                String[] fields = record.split(",");
                for(int i=3; i<fields.length-1; i+=2) {
                    if (productDAO.getProduct(Integer.parseInt(fields[i])) == null) {
                        alert(Alert.AlertType.ERROR, "Error", "Please verify Product References are valid");
                        return;
                    }
                }
            }
            //import orders with respective order lines
            for(String record : records) {
                importRecords.importOrder(record);
            }
            alert(Alert.AlertType.CONFIRMATION, "Success", "Import Completed");
        }
    }

    private void alert(Alert.AlertType type, String title, String content){

        Alert alert = new Alert(type);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/Style.css").toExternalForm());
        alert.initOwner(importPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
