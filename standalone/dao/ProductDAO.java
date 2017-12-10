package test.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import test.entities.Product;
import test.entities.ProductDTO;
import test.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 09/12/2017.
 */
public class ProductDAO {

    private EntityManager em;
    private Session session;

    public ProductDAO() {

        em = JPAUtil.getEntityManager();
    }

    public void openEM() {

        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
    }

    public void closeEM() {

        em.close();
    }

    public Query getAll() {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();

        return (Query) em.createQuery("FROM Product");
    }

    public Query getByProductName(String paramName, String paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByProductName").setParameter(paramName, paramValue);
    }

    public List<Product> getByProductId(Integer number) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        List<Product> products = new ArrayList<>();
        session = em.unwrap(Session.class);
        products.add(em.find(Product.class, number));

        return products;
    }

    public List<Product> getByProductReference(Integer number) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        List<Product> products = new ArrayList<>();
        session = em.unwrap(Session.class);
        products.add(session.bySimpleNaturalId(Product.class).load(number));

        return products;
    }

    public Product getProduct(Integer number) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.bySimpleNaturalId(Product.class).load(number);
    }

    public List<ProductDTO> getProductDTO() {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        return em.createQuery("select new test.entities.ProductDTO (p.reference)" +
                " from Product p", ProductDTO.class).getResultList();
    }

    public void editProductRecord(Product product, String name, Integer stock, Double price) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        product.setName(name);
        product.setStockQuantity(stock);
        product.setPrice(price);
        //product.setImgLink(img);
        em.getTransaction().commit();
    }

    public void deleteProductRecord(Product product) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        em.remove(product);
        em.getTransaction().commit();
    }

    public void insertProductRecord(Product product) throws PersistenceException {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
    }
}
