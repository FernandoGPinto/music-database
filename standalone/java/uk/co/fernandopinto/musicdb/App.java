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

        /*Reader in = new StringReader("a,b,c");
        try {
            for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
                for (String field : record) {
                    System.out.print("\"" + field + "\", ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        /*System.out.println("Hibernate many to many (Annotation)");

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Product product = new Product("cd", "image", 4556, 50, 15);
        em.persist(product);
        product = new Product("vinyl", "image", 5556, 20, 25);
        em.persist(product);

        Order entities = new Order("John", "23 Some Street");
        em.persist(entities);

        Session session = em.unwrap( Session.class );
        Product prod = session
                .bySimpleNaturalId(Product.class)
                .load( 4556 );

        OrderLine orderLine = new OrderLine(4, entities, prod);
        em.persist(orderLine);

        em.getTransaction().commit();

        String hql = "FROM Order";
        Query query = (Query) em.createQuery(hql);
        List<Order> results = query.list();
        for(Order ord:results){
            for(OrderLine ordLine:ord.getLines()) {
                System.out.println("prod name: " + ordLine.getProduct().getName());
            }
        }

        String hql1 = "FROM Product";
        Query query1 = (Query) em.createQuery(hql1);
        List<Product> results1 = query1.list();
        for(Product prod:results1){
            for(OrderLine ordLine:prod.getLines()) {
                System.out.println("ord name: " + ordLine.getOrder().getCustomerName());
            }
        }

        Session session = em.unwrap(Session.class);
        Product prod = session.bySimpleNaturalId(Product.class).load(4556);
        System.out.println("prod price: " + prod.getPrice());

        List<OrderLine> res = em.createQuery("select s " +
                "from OrderLine s " +
                "join fetch s.entities", OrderLine.class)
                .getResultList();

        for(OrderLine ordLine :res){
                System.out.println("prod qty: " + ordLine.getQuantity());
                System.out.println("prod price: " + ordLine.getProduct().getPrice());
                System.out.println("ord address: " + ordLine.getOrder().getCustomerAddress());
        }

        query = session.createNamedQuery("getByOrderId").setParameter("orderId", 1);
        List<Order> o = query.list();
        for(Order ord:o) {
            System.out.println("query ord name: " + ord.getCustomerName());
        }

        query = session.createNamedQuery("getByCustomerName").setParameter("customerName", "John");
        o = query.list();
        for(Order ord:o) {
            System.out.println("query ord date: " + ord.getDate());
        }

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Date begin = null;
        Date end = null;

        try {
            begin = ft.parse("2017-10-12");
            end = ft.parse("2017-10-14");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query = session.createNamedQuery("getByPeriod").setParameter("begin", begin).setParameter("end", end);
        o = query.list();
        for(Order ord:o) {
            System.out.println("query ord id: " + ord.getOrderId());
        }


        em.close();
        JPAUtil.close();

        entities entities = new entities();

        entities.setStockCode("1939");
        entities.setStockName("VINYL");

        StockDetail stockDetail = new StockDetail();
        stockDetail.setCompName("Megamusica");
        stockDetail.setCompDesc("music");
        stockDetail.setRemark("vinci vinci");
        stockDetail.setListedDate(new Date());

        entities.setStockDetail(stockDetail);
        stockDetail.setOrder(entities);

        session.save(entities);
        session.getTransaction().commit();

        System.out.println("Done");*/
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        //
    }

    @Override
    public void stop(){

        JPAUtil.close();
    }
}