package demo.controller;

import demo.model.Order;
import demo.model.OrderLine;
import demo.model.OrderStatus;
import demo.model.ProductDTO;
import demo.repository.OrderLineRepository;
import demo.repository.OrderRepository;
import demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fernando on 01/11/2017.
 */

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class OrderUploadController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @GetMapping(path="/order-upload")
    public String showUpload(Model model) {

        List<ProductDTO> products = productRepository.findAllProductDTO();
        model.addAttribute("products", products);
        return "order-upload";
    }

    @PostMapping(path="/add-order") // Map ONLY Post Requests
    public String addNewOrder(@RequestParam(value="customerName") String customerName,
                              @RequestParam(value="customerAddress") String customerAddress,
                              @RequestParam(value="orderReference") Integer orderReference,
                              @RequestParam(value="products") String[] products, Model model) {

        Order order = new Order(customerName, customerAddress, orderReference);
        for(String product : products){
            OrderLine orderLine = new OrderLine(1, order, productRepository.findByProductReference(Integer.parseInt(product)).get(0));
            orderLineRepository.save(orderLine);
        }
        orderRepository.save(order);
        model.addAttribute("orders", order);
        return "order-upload";
    }

    @PostMapping(value = "/order-upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {

        List<Order> orders = new ArrayList<>();
        if (null != file && !file.isEmpty()) {
            try {
                InputStream inputStream = file.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] info = line.split(",");
                    Order order = new Order(info[0], info[1], Integer.parseInt(info[2]));
                    orderRepository.save(order);
                    orders.add(order);
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("orders", orders);
        return "order-upload";
    }

    /*@RequestMapping(value="/order-upload", params={"addRow"})
    public String addLine(@RequestParam(value="product") String product, @RequestParam(value="quantity") String quantity, final Order order, final BindingResult bindingResult) {
        order.getLines().add(new OrderLine(Integer.parseInt(quantity), order, productRepository.findByProductReference(Integer.parseInt(product)).get(0)));
        return "order-upload";
    }

    @RequestMapping(value="/order-upload", params={"removeRow"})
    public String removeLine(
            final Order order, final BindingResult bindingResult,
            final HttpServletRequest req) {
        final Integer lineId = Integer.valueOf(req.getParameter("removeRow"));
        order.getLines().remove(lineId.intValue());
        return "order-upload";
    }*/
}