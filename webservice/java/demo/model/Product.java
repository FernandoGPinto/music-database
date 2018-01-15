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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "product", catalog = "db_example", uniqueConstraints = {
        @UniqueConstraint(columnNames = "REFERENCE") })
public class Product implements Serializable {

    private Long productId;
    private String productName;
    private String imgLink;
    private Integer productReference;
    private Integer stockQuantity;
    private Double price;
    private Set<OrderLine> lines = new HashSet<>();

    public Product() {
    }

    public Product(String productName, Integer productReference, Integer stockQuantity, Double price, String imgLink) {
        this.productName = productName;
        this.productReference = productReference;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imgLink = imgLink;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "PRODUCT_ID", unique = true, nullable = false)
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) { this.productName = productName; }

    @Column(name = "IMG_LINK", nullable = false)
    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) { this.imgLink = imgLink; }

    @NaturalId
    @Column(name = "REFERENCE", unique = true, nullable = false, length = 10)
    public Integer getProductReference() {
        return productReference;
    }

    public void setProductReference(Integer productReference) {
        this.productReference = productReference;
    }

    @Column(name = "STOCK_QUANTITY", nullable = false, length = 10)
    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Column(name = "PRICE", nullable = false, length = 10)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size=10)
    public Set<OrderLine> getLines() {
        return this.lines;
    }

    public void setLines(Set<OrderLine> lines) {
        this.lines = lines;
    }
}
