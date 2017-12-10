package test.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import test.entities.Order;
import test.entities.OrderLine;
import test.util.JPAUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fernando on 09/12/2017.
 */
public class OrderDAO {

    private EntityManager em;
    private Session session;

    public OrderDAO() {

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

        return (Query) em.createQuery("FROM Order");
    }

    public Query getByCustomerName(String paramName, String paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByCustomerName").setParameter(paramName, paramValue);
    }

    public Query getByDate(LocalDate startDate, LocalDate endDate) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        Date sDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date eDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByPeriod").setParameter("begin", sDate).setParameter("end", eDate);
    }

    public List<Order> getByOrderId(Integer number) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        List<Order> orders = new ArrayList<>();
        orders.add(em.find(Order.class, number));

        return orders;
    }

    public void editOrderRecord(Order order, String name, String address, LocalDate localDate) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        order.setCustomerName(name);
        order.setCustomerAddress(address);
        order.setDate(date);
        //order.setReference(reference);
        em.getTransaction().commit();
    }

    public void deleteOrderRecord(Order order) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        em.remove(order);
        em.getTransaction().commit();
    }

    public void insertOrderRecord(Order order, List<OrderLine> orderLines) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        em.persist(order);
        for(OrderLine orderLine : orderLines) {
            em.persist(orderLine);
        }
        em.getTransaction().commit();
    }
}
