package test;

/**
 * Created by Fernando on 08/10/2017.
 */
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;

import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import test.entities.Order;
import test.entities.OrderLine;
import test.entities.Product;
import test.entities.ProductDTO;
import test.util.JPAUtil;

import javax.persistence.EntityManager;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

public class App extends Application {

    private ObservableList<ObservableList> data;
    private TableView tableView;
    private Integer orderId;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    @Override
    public void stop(){

        JPAUtil.close();
    }
}
