package demo.model;

/**
 * Created by Fernando on 08/10/2017.
 */

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "order", catalog = "db_example")
public class Order implements Serializable {

    private Long orderId;
    private String customerName;
    private String customerAddress;
    private Date date;
    private Integer orderReference;
    private OrderStatus status;
    private Set<OrderLine> lines = new HashSet<>();

    public Order() {
    }

    public Order(String customerName, String customerAddress, Integer orderReference) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.date = new Date();
        this.orderReference = orderReference;
        this.status = OrderStatus.RECEIVED;
    }

    public Order(String customerName, String customerAddress, Set<OrderLine> lines) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.lines = lines;
        this.date = new Date();
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ORDER_ID", unique = true, nullable = false)
    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Column(name = "CUSTOMER_NAME", nullable = false, length = 20)
    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "CUSTOMER_ADDRESS", nullable = false, length = 50)
    public String getCustomerAddress() {
        return this.customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE", nullable = false)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NaturalId
    @Column(name = "REFERENCE", unique = true, nullable = false, length = 10)
    public Integer getOrderReference() {
        return this.orderReference;
    }

    public void setOrderReference(Integer orderReference) {
        this.orderReference = orderReference;
    }

    @Column(name = "STATUS", nullable = false, length = 10)
    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=10)
    public Set<OrderLine> getLines() {
        return this.lines;
    }

    public void setLines(Set<OrderLine> lines) {
        this.lines = lines;
    }

}
