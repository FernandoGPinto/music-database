package test.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Fernando on 11/10/2017.
 */

@NamedQueries({
        @NamedQuery(
                name = "getAllJoin",
                query = "from OrderLine orderLine join fetch orderLine.product p join fetch orderLine.order o"
        ),
        @NamedQuery(
                name = "getByOrderIdJoin",
                query = "from OrderLine orderLine join fetch orderLine.product p join fetch orderLine.order o where o.orderId = :orderId"
        ),
        @NamedQuery(
                name = "getByCustomerNameJoin",
                query = "from OrderLine orderLine join fetch orderLine.product p join fetch orderLine.order o where o.customerName = :customerName"
        ),
        @NamedQuery(
                name = "getByPeriodJoin",
                query = "from OrderLine orderLine join fetch orderLine.product p join fetch orderLine.order o where o.date >= :begin and o.date <= :end"
        ),
        @NamedQuery(
                name = "getByProductIdJoin",
                query = "from OrderLine orderLine join fetch orderLine.order o join fetch orderLine.product p where p.productId = :productId"
        ),
        @NamedQuery(
                name = "getByProductNameJoin",
                query = "from OrderLine orderLine join fetch orderLine.order o join fetch orderLine.product p where p.name = :name"
        ),
        @NamedQuery(
                name = "getByProductReferenceJoin",
                query = "from OrderLine orderLine join fetch orderLine.order o join fetch orderLine.product p where p.reference = :reference"
        )
})

@Entity
@Table(name = "order_line", catalog = "test")
public class OrderLine implements Serializable {

    private Integer orderLineId = null;
    /*@Version
    @Column(name = "version")
    private int version = 0;*/

    private Integer quantity;
    private Order order;
    private Product product;

    public OrderLine() {
    }

    public OrderLine(Integer quantity, Order order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ORDER_LINE_ID", updatable = false, nullable = false)
    public Integer getOrderLineId() {
        return this.orderLineId;
    }

    public void setOrderLineId(Integer orderLineId) {
        this.orderLineId = orderLineId;
    }

    @Column(name = "QUANTITY", nullable = false, length = 10)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Transient
    public String getOrderName() { return order.getCustomerName(); }

    @Transient
    public String getOrderAddress() { return order.getCustomerAddress(); }

    @Transient
    public Date getOrderDate() { return order.getDate(); }

    @Transient
    public String getProductName() { return product.getName(); }

    @Transient
    public Integer getProductReference() { return product.getReference(); }

    @Transient
    public Double getProductPrice() { return product.getPrice(); }
}
