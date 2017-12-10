package test.entities;

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

@NamedQueries({
        @NamedQuery(
                name = "getByReference",
                query = "from Product product where  product.reference = :reference"
        ),
        @NamedQuery(
                name = "getByProductName",
                query = "from Product product where product.name = :name"
        )/*,
        @NamedQuery(
                name = "getByCategory",
                query = "from Product product where product.category = :category"
        )*/
})

@Entity
@Table(name = "product", catalog = "test", uniqueConstraints = {
        @UniqueConstraint(columnNames = "REFERENCE") })
public class Product implements Serializable {

    private Integer productId;
    private String name;
    private String imgLink;
    private Integer reference;
    private Integer stockQuantity;
    private Double price;
    private Set<OrderLine> lines = new HashSet<>();

    public Product() {
    }

    public Product(String name, String imgLink, Integer reference, Integer stockQuantity, Double price) {
        this.name = name;
        this.imgLink = imgLink;
        this.reference = reference ;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "PRODUCT_ID", unique = true, nullable = false)
    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    @Column(name = "IMG_LINK", nullable = false, length = 255)
    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) { this.imgLink = imgLink; }

    @NaturalId
    @Column(name = "REFERENCE", unique = true, nullable = false, length = 10)
    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
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
