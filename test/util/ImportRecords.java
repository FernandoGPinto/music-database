package test.util;

import org.hibernate.Session;
import test.dao.ProductDAO;
import test.entities.Order;
import test.entities.OrderLine;
import test.entities.Product;

import javax.persistence.EntityManager;

/**
 * Created by Fernando on 09/12/2017.
 */
public class ImportRecords {

    private EntityManager em;
    private Session session;

    public ImportRecords() {

        em = JPAUtil.getEntityManager();
    }

    public void openEM() {

        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
    }

    public void closeEM() {

        em.close();
    }

    public void importFile(String fileName) throws Exception {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        em.createNativeQuery("LOAD DATA LOCAL INFILE :fileName INTO TABLE product FIELDS TERMINATED BY ',' LINES TERMINATED BY '\\n' (NAME,IMG_LINK,REFERENCE,STOCK_QUANTITY,PRICE)").setParameter("fileName", fileName).executeUpdate();
        em.getTransaction().commit();
    }

    public void importOrder(String record) throws Exception {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();

        String[] fields = record.split(",");
        Order order = new Order(fields[0], fields[1]);
        em.persist(order);
        ProductDAO productDAO = new ProductDAO();

        for(int i=2; i<fields.length-1; i+=2) {

            Product product = productDAO.getProduct(Integer.parseInt(fields[i+1]));
            OrderLine orderLine = new OrderLine(Integer.parseInt(fields[i]), order, product);
            em.persist(orderLine);
        }
        em.getTransaction().commit();
        //em.getTransaction().rollback();
    }
}
