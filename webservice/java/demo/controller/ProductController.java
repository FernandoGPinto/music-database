package demo.controller;

import demo.model.Product;
import demo.model.ProductDTO;
import demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 01/11/2017.
 */

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path="/product")
    public String showProduct(Model model) {

        return "product";
    }

    @PostMapping(path="/delete-product/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId, Model model, HttpServletRequest request) {

        productRepository.delete(productId);
        List<Product> products = (List<Product>)request.getSession().getAttribute("products");
        for(Product product:products) {
            if(product.getProductId() == productId) {
                products.remove(product);
                break;
            }
        }
        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping(path="/product-edit/{productId}")
    public String showEditView(@PathVariable("productId") Long productId, Model model) {

        model.addAttribute("product", productRepository.findOne(productId));
        return "product-edit";
    }

    @PostMapping(path="/update-product/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @RequestParam(value="productName") String productName,
                              @RequestParam(value="productReference") Integer productReference,
                              @RequestParam(value="stockQuantity") Integer stockQuantity, @RequestParam(value="price") Double price,
                              @RequestParam(value="imgLink") String imgLink,
                              Model model, HttpServletRequest request) {

        Product product = productRepository.findOne(productId);
        product.setProductName(productName);
        product.setStockQuantity(stockQuantity);
        product.setProductReference(productReference);
        product.setPrice(price);
        product.setImgLink(imgLink);
        productRepository.save(product);

        if(request.getSession().getAttribute("products").getClass().toString().equals("class demo.model.Product")) {
            model.addAttribute("products", product);
        } else {
            List<Product> products = (List<Product>)request.getSession().getAttribute("products");
            for(Product prod:products) {
                if(prod.getProductId().equals(productId)) {
                    products.remove(prod);
                    break;
                }
            }
            products.add(0, product);
            model.addAttribute("products", products);
        }
        return "product";
    }

    @GetMapping(path="/get-product")
    public String getProductByIndividualParameter(@RequestParam(value="productId", required=false, defaultValue="") String productId,
                                                @RequestParam(value="productName", required=false, defaultValue="") String productName,
                                                @RequestParam(value="productReference", required=false, defaultValue="") String productReference,
                                                @RequestParam(value="stockQuantity", required=false, defaultValue="") String stockQuantity,
                                                @RequestParam(value="price", required=false, defaultValue="") String price,
                                                @RequestParam(value="imgLink", required=false, defaultValue="") String imgLink,
                                                Model model, HttpServletRequest request){

        List<Product> products;

        if(!productId.equals("")) {
            Product product = productRepository.findOne(Long.parseLong(productId));
            model.addAttribute("products", product);
            request.getSession().setAttribute("products", product);
        }
        if(!productName.equals("")) {
            products = productRepository.findByProductName(productName);
            model.addAttribute("products", products);
            request.getSession().setAttribute("products", products);
        }
        if(!productReference.equals("")) {
            products = productRepository.findByProductReference(Integer.parseInt(productReference));
            model.addAttribute("products", products);
            request.getSession().setAttribute("products", products);
        }
        if(!stockQuantity.equals("")) {
            products = productRepository.findByStockQuantity(Integer.parseInt(stockQuantity));
            model.addAttribute("products", products);
            request.getSession().setAttribute("products", products);
        }
        if(!price.equals("")) {
            products = productRepository.findByPrice(Double.parseDouble(price));
            model.addAttribute("products", products);
            request.getSession().setAttribute("products", products);
        }
        if(!imgLink.equals("")) {
            products = productRepository.findByImgLink(imgLink);
            model.addAttribute("products", products);
            request.getSession().setAttribute("products", products);
        }
        return "product";
    }
}
