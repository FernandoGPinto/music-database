package test.entities;

/**
 * Created by Fernando on 26/10/2017.
 */
public class ProductDTO {

    private final Integer productReference;

    public ProductDTO(Integer productReference) {

        this.productReference = productReference;
    }

    public Integer getProductReference() {

        return productReference;
    }
}
