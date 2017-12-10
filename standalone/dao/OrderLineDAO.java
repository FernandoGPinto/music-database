package test.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import test.entities.OrderLine;
import test.util.JPAUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Fernando on 09/12/2017.
 */
public class OrderLineDAO {

    private EntityManager em;
    private Session session;

    public OrderLineDAO() {

        em = JPAUtil.getEntityManager();
    }

    public void openEM() {

        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
    }

    public void closeEM() {

        em.close();
    }

    public Query getAllJoin() {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getAllJoin");
    }

    public Query getByOrderIdJoin(String paramName, Integer paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByOrderIdJoin").setParameter(paramName, paramValue);
    }

    public Query getByProductIdJoin(String paramName, Integer paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByProductIdJoin").setParameter(paramName, paramValue);
    }

    public Query getByProductReferenceJoin(String paramName, Integer paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByProductReferenceJoin").setParameter(paramName, paramValue);
    }

    public Query getByProductNameJoin(String paramName, String paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByProductNameJoin").setParameter(paramName, paramValue);
    }

    public Query getByCustomerNameJoin(String paramName, String paramValue) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByCustomerNameJoin").setParameter(paramName, paramValue);
    }

    public Query getByPeriodJoin(LocalDate startDate, LocalDate endDate) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        Date sDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date eDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        session = em.unwrap(Session.class);

        return session.createNamedQuery("getByPeriodJoin").setParameter("begin", sDate).setParameter("end", eDate);
    }

    public void editOrderLineRecord(OrderLine orderLine, Integer quantity, String name, String address, LocalDate localDate) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        orderLine.setQuantity(quantity);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        orderLine.getOrder().setCustomerName(name);
        orderLine.getOrder().setCustomerAddress(address);
        orderLine.getOrder().setDate(date);
        //order.setReference(reference);
        em.getTransaction().commit();
    }

    public void deleteOrderLineRecord(OrderLine orderLine) {

        if(!em.getTransaction().isActive()) em.getTransaction().begin();
        em.remove(orderLine);
        em.getTransaction().commit();
    }
}
