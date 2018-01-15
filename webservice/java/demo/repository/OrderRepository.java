package demo.repository;

import demo.model.Order;
import demo.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Fernando on 01/11/2017.
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

    //Use @Query or method queries to populate with custom queries
    //can create native queries for import function

    List<Order> findByCustomerName(@Param("customerName")String customerName);

    List<Order> findByCustomerAddress(@Param("customerAddress")String customerAddress);

    List<Order> findByOrderReference(@Param("orderReference")Integer orderReference);

    List<Order> findByDate(@Param("date")Date date);

    List<Order> findByStatus(@Param("status")OrderStatus status);
}
