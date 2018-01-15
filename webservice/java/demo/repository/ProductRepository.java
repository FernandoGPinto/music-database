package demo.repository;

import demo.model.Order;
import demo.model.OrderStatus;
import demo.model.Product;
import demo.model.ProductDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Fernando on 01/11/2017.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByProductName(@Param("productName")String productName);

    List<Product> findByProductReference(@Param("productReference")Integer productReference);

    List<Product> findByStockQuantity(@Param("stockQuantity")Integer stockQuantity);

    List<Product> findByPrice(@Param("price")Double price);

    List<Product> findByImgLink(@Param("imgLink")String imgLink);

    @Query("select new demo.model.ProductDTO (p.productReference) from Product p")
    List<ProductDTO> findAllProductDTO();
}
