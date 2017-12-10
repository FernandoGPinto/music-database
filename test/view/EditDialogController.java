package test.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import test.dao.OrderDAO;
import test.dao.OrderLineDAO;
import test.dao.ProductDAO;
import test.entities.Order;
import test.entities.OrderLine;
import test.entities.Product;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * Created by Fernando on 23/10/2017.
 */
public class EditDialogController implements Initializable {

    @FXML private AnchorPane dialogAP;
    @FXML private GridPane dialogGP;

    private TextField nameField = new TextField();
    private TextField addressField = new TextField();
    private DatePicker dateField = new DatePicker();
    private TextField referenceField = new TextField();
    private TextField stockField = new TextField();
    private TextField priceField = new TextField();
    private TextField quantityField = new TextField();

    private Order order = null;
    private OrderLine orderLine = null;
    private Product product = null;
    private OrderDAO orderDAO;
    private OrderLineDAO orderLineDAO;
    private ProductDAO productDAO;

    void setFields(Order order, OrderDAO orderDAO) {

        this.order = order;
        this.orderDAO = orderDAO;
        dialogGP.add(new Label("Name:"), 0, 0);
        nameField.setText(order.getCustomerName());
        dialogGP.add(nameField, 1, 0);
        dialogGP.add(new Label("Address:"), 0, 1);
        addressField.setText(order.getCustomerAddress());
        dialogGP.add(addressField, 1, 1);
        dialogGP.add(new Label("Date:"), 0, 2);
        dateField.setValue(Instant.ofEpochMilli(order.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        dialogGP.add(dateField, 1, 2);
        /*dialogGP.add(new Label("Reference:"), 0, 3);
        dialogGP.add(new TextField(order.getReference().toString()), 1, 3);*/
    }

    void setFields(OrderLine orderLine, OrderLineDAO orderLineDAO) {

        this.orderLine = orderLine;
        this.orderLineDAO = orderLineDAO;
        dialogGP.add(new Label("Quantity:"), 0, 0);
        quantityField.setText(String.valueOf(orderLine.getQuantity()));
        dialogGP.add(quantityField, 1, 0);
        dialogGP.add(new Label("Name:"), 0, 1);
        nameField.setText(orderLine.getOrderName());
        dialogGP.add(nameField, 1, 1);
        dialogGP.add(new Label("Address:"), 0, 2);
        addressField.setText(orderLine.getOrderAddress());
        dialogGP.add(addressField, 1, 2);
        dialogGP.add(new Label("Date:"), 0, 3);
        dateField.setValue(Instant.ofEpochMilli(orderLine.getOrderDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        dialogGP.add(dateField, 1, 3);
    }

    void setFields(Product product, ProductDAO productDAO) {

        this.product = product;
        this.productDAO = productDAO;
        dialogGP.add(new Label("Name:"), 0, 0);
        nameField.setText(product.getName());
        dialogGP.add(nameField, 1, 0);
        dialogGP.add(new Label("Stock:"), 0, 1);
        stockField.setText(String.valueOf(product.getStockQuantity()));
        dialogGP.add(stockField, 1, 1);
        dialogGP.add(new Label("Price:"), 0, 2);
        priceField.setText(String.valueOf(product.getPrice()));
        dialogGP.add(priceField, 1, 2);
    }

    public void handleOk() {

        try {
            if (order != null) orderDAO.editOrderRecord(order, nameField.getText(), addressField.getText(), dateField.getValue());
            if (product != null) productDAO.editProductRecord(product, nameField.getText(), Integer.valueOf(stockField.getText()),
                    Double.valueOf(priceField.getText()));
            if (orderLine != null) orderLineDAO.editOrderLineRecord(orderLine, Integer.parseInt(quantityField.getText()),
                    nameField.getText(), addressField.getText(), dateField.getValue());
            Stage stage = (Stage) dialogAP.getScene().getWindow();
            stage.close();
        } catch(NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Error", "Please correct invalid fields");
        }
    }

    public void handleCancel() {

        Stage stage = (Stage) dialogAP.getScene().getWindow();
        stage.close();
    }

    private void alert(Alert.AlertType type, String title, String content){

        Alert alert = new Alert(type);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/Style.css").toExternalForm());
        alert.initOwner(dialogAP.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
