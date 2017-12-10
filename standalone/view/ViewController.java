package test.view;

/**
 * Created by Fernando on 24/10/2017.
 */
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import test.dao.OrderDAO;
import test.dao.OrderLineDAO;
import test.dao.ProductDAO;
import test.entities.Order;
import test.entities.OrderLine;
import test.entities.Product;
import test.util.ExportRecords;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class ViewController implements Initializable {

    @FXML private GridPane gridPane;
    @FXML private StackPane flipPane;
    @FXML private TableView tableView;
    @FXML private AnchorPane anchorPane;
    @FXML private TextField orderIdText;
    @FXML private TextField orderNameText;
    @FXML private TextField productIdText;
    @FXML private TextField productReferenceText;
    @FXML private TextField productNameText;
    @FXML private DatePicker orderStartDate;
    @FXML private DatePicker orderEndDate;

    //variables for flip animation
    private static final Double PIE = Math.PI;
    private static final Double HALF_PIE = Math.PI / 2;
    private static final double ANIMATION_DURATION = 10000;
    private static final double ANIMATION_RATE = 10;
    private Timeline animation;
    private SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);
    private PerspectiveTransform transform = new PerspectiveTransform();
    private SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);

    private String query;
    private String entityType;
    private boolean link = false;
    private OrderDAO orderDAO = new OrderDAO();
    private OrderLineDAO orderLineDAO = new OrderLineDAO();
    private ProductDAO productDAO = new ProductDAO();

    @FXML
    private void handleOrdProd(MouseEvent evt) {

        int row = gridPane.getRowIndex((Node)evt.getSource());
        int col = gridPane.getColumnIndex((Node)evt.getSource());
        for(Node node:gridPane.getChildren())  {
            if((gridPane.getColumnIndex(node) == (col+1) && gridPane.getRowIndex(node) >= row
                    && gridPane.getRowIndex(node) <= row+4) || gridPane.getColumnIndex(node) == col
                    || (gridPane.getColumnIndex(node) == (col+2)
                    && (gridPane.getRowIndex(node) == row) || (gridPane.getRowIndex(node) == row+4))){
                node.setDisable(false);
            } else {
                node.setDisable(true);
            }
        }
    }

    @FXML
    private void handleSelection(MouseEvent evt) {

        //enable relevant fields and disable all others
        int row = gridPane.getRowIndex((Node)evt.getSource());
        int col = gridPane.getColumnIndex((Node)evt.getSource());
        int nodeRow;
        int nodeCol;
        for(Node node:gridPane.getChildren())  {
            nodeRow = gridPane.getRowIndex(node);
            nodeCol = gridPane.getColumnIndex(node);
            if(nodeRow == row && nodeCol == (col+1)) node.setDisable(false);
            if(nodeCol == (col+1) && !(nodeRow == row || nodeRow == 2 || nodeRow == 6 || nodeRow == 7 || nodeRow == 11)
                    || nodeCol == (col+2)) {
                node.setDisable(true);
            }
        }
        query = "other";
    }

    @FXML
    private void handleAllOrders() {

        query = "allOrders";
        Node node = gridPane.getChildren().get(15); //enable view data button
        node.setDisable(false);
        //disable all text fields
        node = gridPane.getChildren().get(2);
        node.setDisable(true);
        node = gridPane.getChildren().get(3);
        node.setDisable(true);
        node = gridPane.getChildren().get(4);
        node.setDisable(true);
    }

    @FXML
    private void handleAllProducts() {

        query = "allProducts";
        Node node = gridPane.getChildren().get(15); //enable view data button
        node.setDisable(false);
        //disable all text fields
        node = gridPane.getChildren().get(12);
        node.setDisable(true);
        node = gridPane.getChildren().get(13);
        node.setDisable(true);
        node = gridPane.getChildren().get(14);
        node.setDisable(true);
    }

    @FXML
    private void handleLinkYes() {

        link = true;
    }

    @FXML
    private void handleLinkNo() {

        link = false;
    }

    @FXML
    private void handleEnableViewData() {

        Node node = gridPane.getChildren().get(15);
        node.setDisable(false);
    }

    @FXML
    private void handleViewData() {

        //Call dialog window while loading to avoid concurrency issues
        switch(query) {
            case "allOrders":
                if(!link) {
                    List<Order> orders = orderDAO.getAll().list();
                    setupOrderTable(FXCollections.observableArrayList(orders));
                }
                if(link) {
                    List<OrderLine> orderLines = orderLineDAO.getAllJoin().list();
                    setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                }
                break;
            case "allProducts":
                if(!link) {
                    List<Product> products = productDAO.getAll().list();
                    setupProductTable(FXCollections.observableArrayList(products));
                }
                if(link) {
                    List<OrderLine> orderLines = orderLineDAO.getAllJoin().list();
                    setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                }
                break;
            case "other":
                if(link) {
                    if(!orderIdText.isDisabled()) {
                        List<OrderLine> orderLines = orderLineDAO.getByOrderIdJoin("orderId", Integer.parseInt(orderIdText.getText())).getResultList();
                        setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                    }
                    if(!orderNameText.isDisabled()) {
                        List<OrderLine> orderLines = orderLineDAO.getByCustomerNameJoin("customerName" , orderNameText.getText()).getResultList();
                        setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                    }
                    if(!orderStartDate.isDisabled()) {
                        List<OrderLine> orderLines = orderLineDAO.getByPeriodJoin(orderStartDate.getValue(), orderEndDate.getValue()).getResultList();
                        setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                    }
                    if(!productIdText.isDisabled()) {
                        List<OrderLine> orderLines = orderLineDAO.getByProductIdJoin("productId", Integer.parseInt(productIdText.getText())).getResultList();
                        setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                    }
                    if(!productReferenceText.isDisabled()) {
                        List<OrderLine> orderLines = orderLineDAO.getByProductReferenceJoin("reference", Integer.parseInt(productReferenceText.getText())).getResultList();
                        setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                    }
                    if(!productNameText.isDisabled()) {
                        List<OrderLine> orderLines = orderLineDAO.getByProductNameJoin("name" , productNameText.getText()).getResultList();
                        setupOrderLineTable(FXCollections.observableArrayList(orderLines));
                    }
                }
                if(!link) {
                    if(!orderIdText.isDisabled()) {
                        List<Order> orders = orderDAO.getByOrderId(Integer.parseInt(orderIdText.getText()));
                        setupOrderTable(FXCollections.observableArrayList(orders));
                    }
                    if(!orderNameText.isDisabled()) {
                        List<Order> orders = orderDAO.getByCustomerName("customerName" , orderNameText.getText()).getResultList();
                        setupOrderTable(FXCollections.observableArrayList(orders));
                    }
                    if(!orderStartDate.isDisabled()) {
                        List<Order> orders = orderDAO.getByDate(orderStartDate.getValue(), orderEndDate.getValue()).getResultList();
                        setupOrderTable(FXCollections.observableArrayList(orders));
                    }
                    if(!productIdText.isDisabled()) {
                        List<Product> products = productDAO.getByProductId(Integer.parseInt(productIdText.getText()));
                        setupProductTable(FXCollections.observableArrayList(products));
                    }
                    if(!productReferenceText.isDisabled()) {
                        List<Product> products = productDAO.getByProductReference(Integer.parseInt(productReferenceText.getText()));
                        setupProductTable(FXCollections.observableArrayList(products));
                    }
                    if(!productNameText.isDisabled()) {
                        List<Product> products = productDAO.getByProductName("name", productNameText.getText()).getResultList();
                        setupProductTable(FXCollections.observableArrayList(products));
                    }
                }
                break;
            default:
                System.out.println("Nocando");
                break;
        }
        flip();
    }

    @FXML
    private void handleEditRecords() {

        if(tableView.getSelectionModel().getSelectedItem() != null) {
            //instantiate dialog in home controller or main?
            Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Record");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditDialog.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            EditDialogController controller = loader.getController();
            switch (entityType) {
                case "Order":
                    Order selectedOrder = (Order) tableView.getSelectionModel().getSelectedItem();
                    controller.setFields(selectedOrder, orderDAO);
                    break;
                case "Product":
                    Product selectedProduct = (Product) tableView.getSelectionModel().getSelectedItem();
                    controller.setFields(selectedProduct, productDAO);
                    break;
                case "OrderLine":
                    OrderLine selectedOrderLine = (OrderLine) tableView.getSelectionModel().getSelectedItem();
                    controller.setFields(selectedOrderLine, orderLineDAO);
                    break;
                default:
                    //handle error
            }
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            tableView.refresh();
        }
    }

    @FXML
    public void handleDeleteRecords() {

        switch(entityType) {
            case "Order" :
                Order selectedOrder = (Order) tableView.getSelectionModel().getSelectedItem();
                orderDAO.deleteOrderRecord(selectedOrder);
                break;
            case "Product" :
                Product selectedProduct = (Product) tableView.getSelectionModel().getSelectedItem();
                productDAO.deleteProductRecord(selectedProduct);
                break;
            case "OrderLine" :
                OrderLine selectedOrderLine = (OrderLine) tableView.getSelectionModel().getSelectedItem();
                orderLineDAO.deleteOrderLineRecord(selectedOrderLine);
                break;
            default :
                //handle error
        }
        tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
        tableView.refresh();
    }

    @FXML
    public void handleNewRecords() {

        Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("New Record");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewDialog.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewDialogController controller = loader.getController();
        switch (entityType) {
            case "Order":
            case "OrderLine":
                controller.setOrderFields(tableView, orderDAO, entityType);
                break;
            case "Product":
                controller.setProductFields(tableView, productDAO, entityType);
                break;
            default:
                alert(Alert.AlertType.ERROR, "Error", "Unrecognised entity type. " +
                        "Please close and try again or request technical support.");
                break;
        }
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.setResizable(true);
        dialogStage.showAndWait();
        tableView.refresh();
    }

    @FXML
    public void handleExportRecords() {

        ExportRecords exportRecords = null;

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose directory where to save records.csv file");
        String selectedDirectory = chooser.showDialog(anchorPane.getScene().getWindow()).getAbsolutePath();
        selectedDirectory += "\\records.csv";

        switch(entityType) {
            case "Order" :
                List<Order> orders = tableView.getItems();
                try {
                    exportRecords = new ExportRecords(selectedDirectory);
                    for(Order order : orders){
                        List<String> orderRecord = new ArrayList<>();
                        orderRecord.add(String.valueOf(order.getOrderId()));
                        orderRecord.add(order.getCustomerName());
                        orderRecord.add(order.getCustomerAddress());
                        orderRecord.add(order.getDate().toString());
                        exportRecords.writeToFile(orderRecord);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    alert(Alert.AlertType.ERROR, "Error", "Error occurred while writing to file. " +
                            "Please try again or request technical support.");
                } finally {
                    try {
                        exportRecords.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        alert(Alert.AlertType.ERROR, "Error", "Error occurred while closing file. " +
                                "Please request technical support.");
                    }
                }
                break;
            case "Product" :
                List<Product> products = tableView.getItems();
                try {
                    exportRecords = new ExportRecords(selectedDirectory);
                    for(Product product : products){
                        List<String> productRecord = new ArrayList<>();
                        productRecord.add(String.valueOf(product.getProductId()));
                        productRecord.add(product.getName());
                        productRecord.add(String.valueOf(product.getReference()));
                        productRecord.add(String.valueOf(product.getStockQuantity()));
                        productRecord.add(String.valueOf(product.getPrice()));
                        productRecord.add(product.getImgLink());
                        exportRecords.writeToFile(productRecord);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    alert(Alert.AlertType.ERROR, "Error", "Error occurred while writing to file. " +
                            "Please try again or request technical support.");
                } finally {
                    try {
                        exportRecords.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        alert(Alert.AlertType.ERROR, "Error", "Error occurred while closing file. " +
                                "Please request technical support.");
                    }
                }
                break;
            case "OrderLine" :
                List<OrderLine> orderLines = tableView.getItems();
                try {
                    exportRecords = new ExportRecords(selectedDirectory);
                    for(OrderLine orderLine : orderLines){
                        List<String> orderLineRecord = new ArrayList<>();
                        orderLineRecord.add(String.valueOf(orderLine.getOrderLineId()));
                        orderLineRecord.add(orderLine.getOrderName());
                        orderLineRecord.add(orderLine.getOrderAddress());
                        orderLineRecord.add(orderLine.getProductName());
                        orderLineRecord.add(String.valueOf(orderLine.getProductReference()));
                        orderLineRecord.add(String.valueOf(orderLine.getQuantity()));
                        orderLineRecord.add(String.valueOf(orderLine.getProductPrice()));
                        orderLineRecord.add(orderLine.getOrderDate().toString());
                        exportRecords.writeToFile(orderLineRecord);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    alert(Alert.AlertType.ERROR, "Error", "Error occurred while writing to file. " +
                            "Please try again or request technical support.");
                } finally {
                    try {
                        exportRecords.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        alert(Alert.AlertType.ERROR, "Error", "Error occurred while closing file. " +
                                "Please request technical support.");
                    }
                }
                break;
            default :
                alert(Alert.AlertType.ERROR, "Error", "Unrecognised entity type. " +
                        "Please close and try again or request technical support.");
        }
    }

    @FXML
    public void handleReturn() {

        for(Node node:gridPane.getChildren())  {
            if(gridPane.getColumnIndex(node) == 1) node.setDisable(false);
            else node.setDisable(true);
        }
        link = false;
        flip();
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    private void setupOrderTable(ObservableList<Order> orderList) {

        entityType = "Order";

        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("Customer Name");
        TableColumn address = new TableColumn("Customer Address");
        TableColumn date = new TableColumn("Date");

        id.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        name.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<Order, String>("customerAddress"));
        date.setCellValueFactory(new PropertyValueFactory<Order, Date>("date"));

        id.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        name.minWidthProperty().bind(tableView.widthProperty().multiply(0.27));
        address.minWidthProperty().bind(tableView.widthProperty().multiply(0.38));
        date.minWidthProperty().bind(tableView.widthProperty().multiply(0.23));
        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(id, name, address, date);
        tableView.setItems(orderList);
        //queryManager.closeEM();
    }

    private void setupProductTable(ObservableList<Product> productList) {

        entityType = "Product";

        TableColumn id = new TableColumn("ID");
        TableColumn imgLink = new TableColumn("Image Link");
        TableColumn name = new TableColumn("Product Name");
        TableColumn reference = new TableColumn("Reference");
        TableColumn price = new TableColumn("Price/Unit");
        TableColumn stockQuantity = new TableColumn("Stock");

        id.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productId"));
        imgLink.setCellValueFactory(new PropertyValueFactory<Product, String>("imgLink"));
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        reference.setCellValueFactory(new PropertyValueFactory<Product, Integer>("reference"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        stockQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stockQuantity"));

        id.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        imgLink.minWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        name.minWidthProperty().bind(tableView.widthProperty().multiply(0.28));
        reference.minWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        price.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        stockQuantity.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));

        tableView.getColumns().addAll(id, name, reference, price, stockQuantity, imgLink);
        tableView.setItems(productList);
        //queryManager.closeEM();
    }

    private void setupOrderLineTable(ObservableList<OrderLine> orderLineList) {

        entityType = "OrderLine";

        TableColumn id = new TableColumn("ID");
        TableColumn customerName = new TableColumn("Customer Name");
        TableColumn address = new TableColumn("Customer Address");
        TableColumn date = new TableColumn("Date");
        TableColumn productReference = new TableColumn("Product Ref");
        TableColumn productName = new TableColumn("Product Name");
        TableColumn price = new TableColumn("Price/Unit");
        TableColumn quantity = new TableColumn("Quantity");
        //TableColumn total = new TableColumn("Total");

        id.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("orderLineId"));
        customerName.setCellValueFactory(new PropertyValueFactory<Order, String>("orderName"));
        address.setCellValueFactory(new PropertyValueFactory<Order, String>("orderAddress"));
        date.setCellValueFactory(new PropertyValueFactory<Order, Date>("orderDate"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
        productReference.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productReference"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Double>("productPrice"));
        quantity.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("quantity"));
        //total.setCellValueFactory(new PropertyValueFactory<OrderLine, ReadOnlyDoubleProperty>("Total"));

        id.minWidthProperty().bind(tableView.widthProperty().multiply(0.07));
        customerName.minWidthProperty().bind(tableView.widthProperty().multiply(0.17));
        address.minWidthProperty().bind(tableView.widthProperty().multiply(0.20));
        date.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        productName.minWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        productReference.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        price.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        quantity.minWidthProperty().bind(tableView.widthProperty().multiply(0.1));

        tableView.getColumns().addAll(id, customerName, address, date, productName, productReference, price, quantity);
        tableView.setItems(orderLineList);
        //queryManager.closeEM();
    }

    private Timeline createAnimation() {

        return new Timeline(

                new KeyFrame(Duration.millis(0), new KeyValue(angle, HALF_PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, 0, Interpolator.EASE_IN)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), arg0 -> flippedProperty.set( flippedProperty.not().get() )),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2),  new KeyValue(angle, PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION), new KeyValue(angle, HALF_PIE, Interpolator.EASE_OUT))
        );
    }

    private void flip() {

        if (animation == null) animation = createAnimation();
        animation.setRate(flippedProperty.get() ? ANIMATION_RATE : -ANIMATION_RATE);
        animation.play();
    }

    private SimpleDoubleProperty createAngleProperty() {

        final SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);
        angle.addListener((obsValue, oldValue, newValue) -> recalculateTransformation(newValue.doubleValue()));
        return angle;
    }

    private void setupFlipPane() {

        angle = createAngleProperty();
        gridPane.setEffect(transform);
        gridPane.visibleProperty().bind(flippedProperty);
        anchorPane.setEffect(transform);
        anchorPane.visibleProperty().bind(flippedProperty.not());

        flipPane.widthProperty().addListener((arg0, arg1, arg2) -> recalculateTransformation(angle.doubleValue()));
        flipPane.heightProperty().addListener((arg0, arg1, arg2) -> recalculateTransformation(angle.doubleValue()));
    }

    private void recalculateTransformation(final double angle) {

        final double insetsTop = flipPane.getInsets().getTop() * 2;
        final double insetsLeft = flipPane.getInsets().getLeft() * 2;
        final double radius = flipPane.widthProperty().subtract(insetsLeft).divide(2).doubleValue();
        final double height = flipPane.heightProperty().subtract(insetsTop).doubleValue();
        final double back = height / 10;

        //Compute transform: "Affine Transformation - Rotation"
        transform.setUlx(radius - Math.sin(angle) * radius);
        transform.setUly(0 - Math.cos(angle) * back);
        transform.setUrx(radius + Math.sin(angle) * radius);
        transform.setUry(0 + Math.cos(angle) * back);
        transform.setLrx(radius + Math.sin(angle) * radius);
        transform.setLry(height - Math.cos(angle) * back);
        transform.setLlx(radius - Math.sin(angle) * radius);
        transform.setLly(height + Math.cos(angle) * back);
    }

    private void alert(Alert.AlertType type, String title, String content){

        Alert alert = new Alert(type);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/Style.css").toExternalForm());
        alert.initOwner(anchorPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setupFlipPane();
    }
}