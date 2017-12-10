package test.entities;

/**
 * Created by Fernando on 08/10/2017.
 */

import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@NamedQueries({
        @NamedQuery(
                name = "getByCustomerName",
                query = "from Order order where order.customerName = :customerName"
        ),
        @NamedQuery(
                name = "getByPeriod",
                query = "from Order order where order.date >= :begin and order.date <= :end"
        )
})

@Entity
@Table(name = "order", catalog = "test")
public class Order implements Serializable {

    private Integer orderId;
    private String customerName;
    private String customerAddress;
    private Date date;
    private Set<OrderLine> lines = new HashSet<>();

    public Order() {
    }

    public Order(String customerName, String customerAddress) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.date = new Date();
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
    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Column(name = "CUSTOMER_NAME", unique = true, nullable = false, length = 20)
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

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=10)
    public Set<OrderLine> getLines() {
        return this.lines;
    }

    public void setLines(Set<OrderLine> lines) {
        this.lines = lines;
    }

}
