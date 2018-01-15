package demo.controller;

import demo.model.Product;
import demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 06/11/2017.
 */
@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class ProductUploadController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path="/product-upload")
    public String showProductUpload(Model model) {

        return "product-upload";
    }

    @PostMapping(path="/add-product") // Map ONLY Post Requests
    public String addNewProduct(@RequestParam(value="productName") String productName,
                                @RequestParam(value="stockQuantity") Integer stockQuantity,
                                @RequestParam(value="productReference") Integer productReference,
                                @RequestParam(value="price") Double price,
                                @RequestParam(value="imgLink") String imgLink, Model model) {

        Product product = new Product(productName, productReference, stockQuantity, price, imgLink);
        productRepository.save(product);
        model.addAttribute("products", product);
        return "product-upload";
    }

    @PostMapping(value = "/product-upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {

        List<Product> products = new ArrayList<>();
        if (null != file && !file.isEmpty()) {
            try {
                InputStream inputStream = file.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] info = line.split(",");
                    Product product = new Product(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]),
                            Double.parseDouble(info[3]), info[4]);
                    productRepository.save(product);
                    products.add(product);
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("products", products);
        return "product-upload";
    }
}
