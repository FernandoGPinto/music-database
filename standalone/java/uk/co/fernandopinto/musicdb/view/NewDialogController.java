package test.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import test.dao.OrderDAO;
import test.dao.ProductDAO;
import test.entities.Order;
import test.entities.OrderLine;
import test.entities.Product;
import test.entities.ProductDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Fernando on 26/10/2017.
 */
public class NewDialogController  implements Initializable {

    @FXML private ScrollPane dialogAP;
    @FXML private GridPane dialogGP;

    private TableView tableView;
    private TextField nameField = new TextField();
    private TextField addressField = new TextField();
    private TextField stockField = new TextField();
    private TextField priceField = new TextField();
    private TextField quantityField = new TextField();
    private TextField referenceField = new TextField();
    private TextField imgField = new TextField();
    private ComboBox<Integer> productReferences = new ComboBox<>();

    private String entityType = null;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private Map<Integer, Integer> products = new HashMap<>();

    void setOrderFields(TableView tableView, OrderDAO orderDAO, String entityType) {

        this.orderDAO = orderDAO;
        this.entityType = entityType;
        this.tableView = tableView;
        dialogGP.add(new Label("Name:"), 0, 0);
        dialogGP.add(nameField, 1, 0);
        dialogGP.add(new Label("Address:"), 0, 1);
        dialogGP.add(addressField, 1, 1);
        dialogGP.add(new Label("Product Ref:"), 0, 2);
        setupComboBox();
        dialogGP.add(productReferences, 1, 2);
        dialogGP.add(new Label("Quantity:"), 0, 3);
        dialogGP.add(quantityField, 1, 3);
        Button addProduct = new Button("Add Product");
        addProduct.setOnMouseClicked(event -> {
            if(productReferences.getValue() != null && !quantityField.getText().isEmpty()) {
                if (!products.containsKey(productReferences.getValue())) {
                    try {
                        products.put(productReferences.getValue(), Integer.parseInt(quantityField.getText()));
                        dialogGP.addColumn(2, new Label(productReferences.getValue().toString()));
                        dialogGP.addColumn(3, new Label(quantityField.getText()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        alert(Alert.AlertType.ERROR, "Error", "Some fields have unsupported character types. " +
                                "Please correct invalid fields");
                    }
                } else {
                    alert(Alert.AlertType.ERROR, "Error", "This Product Reference has already been selected.");
                }
            }
        });
        dialogGP.add(addProduct, 2, 0);
        Button removeProduct = new Button("Remove Product");
        removeProduct.setOnMouseClicked(event -> {
            if(products.size() != 0) {
                int last = dialogGP.getChildren().size();
                Label label = (Label) dialogGP.getChildren().get(last - 2);
                products.remove(Integer.parseInt(label.getText()));
                dialogGP.getChildren().remove(last - 2, last);
            } else {
                alert(Alert.AlertType.INFORMATION, "", "Cannot remove: Product List is empty.");
            }
        });
        dialogGP.add(removeProduct, 3, 0);
        dialogGP.add(new Label("Product Ref"), 2, 1);
        dialogGP.add(new Label("Quantity"), 3, 1);
    }

    private void setupComboBox() {

        List<ProductDTO> list = productDAO.getProductDTO();
        List<Integer> references = new ArrayList<>();
        for(ProductDTO prod : list) {
            references.add(prod.getProductReference());
        }
        productReferences.setItems(FXCollections.observableArrayList(references));
    }

    void setProductFields(TableView tableView, ProductDAO productDAO, String entityType) {

        this.productDAO = productDAO;
        this.entityType = entityType;
        this.tableView = tableView;
        dialogGP.add(new Label("Name:"), 0, 0);
        dialogGP.add(nameField, 1, 0);
        dialogGP.add(new Label("Product Ref:"),0, 1);
        dialogGP.add(referenceField, 1, 1);
        dialogGP.add(new Label("Stock Quantity:"), 0, 2);
        dialogGP.add(stockField, 1, 2);
        dialogGP.add(new Label("Price:"), 0, 3);
        dialogGP.add(priceField, 1, 3);
        dialogGP.add(new Label("Img Link:"), 0, 4);
        dialogGP.add(imgField, 1, 4);
    }

    @FXML
    public void handleOk() {

        Stage stage = (Stage) dialogAP.getScene().getWindow();
        List<OrderLine> orderLines = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        Order order;
        Product product;

        switch (entityType) {
            case "Order" :
            case "OrderLine" :
                if(addressField.getText().isEmpty() || nameField.getText().isEmpty() || products.isEmpty()) {
                    alert(Alert.AlertType.ERROR, "Error", "Please complete all fields " +
                            "and add at least one product.");
                    return;
                }
                order = new Order(nameField.getText(), addressField.getText());
                orders.add(order);
                for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                    product = productDAO.getProduct(entry.getKey());
                    OrderLine orderLine = new OrderLine(entry.getValue(), order, product);
                    orderLines.add(orderLine);
                }
                orderDAO.insertOrderRecord(order, orderLines);
                switch (entityType) {
                    case "Order" :
                        tableView.getItems().addAll(FXCollections.observableArrayList(orders));
                        break;
                    case "OrderLine" :
                        tableView.getItems().addAll(FXCollections.observableArrayList(orderLines));
                        break;
                }
                stage.close();
                break;
            case "Product" :
                if(nameField.getText().isEmpty() || referenceField.getText().isEmpty() || imgField.getText().isEmpty()
                        || stockField.getText().isEmpty() || priceField.getText().isEmpty()) {
                    alert(Alert.AlertType.ERROR, "Error", "Please complete all fields.");
                    return;
                }
                try {
                    product = new Product(nameField.getText(), imgField.getText(), Integer.parseInt(referenceField.getText()),
                            Integer.parseInt(stockField.getText()), Double.parseDouble(priceField.getText()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    alert(Alert.AlertType.ERROR, "Error", "Some fields have unsupported character types. " +
                                "Please correct invalid fields");
                    return;
                }
                try {
                    productDAO.insertProductRecord(product);
                    productList.add(product);
                    tableView.getItems().addAll(FXCollections.observableArrayList(productList));
                } catch (Exception e) {
                    alert(Alert.AlertType.ERROR, "Error", "Product reference " + referenceField.getText() + " already exists. " +
                            "Please close and try again");
                }
                stage.close();
                break;
            default :
                alert(Alert.AlertType.ERROR, "Error", "Unrecognised entity type. " +
                        "Please close and try again or request technical support.");
                break;
        }
    }

    @FXML
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
