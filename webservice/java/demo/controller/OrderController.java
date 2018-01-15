package demo.controller;

import demo.model.Order;
import demo.model.OrderLine;
import demo.model.OrderStatus;
import demo.model.Product;
import demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fernando on 01/11/2017.
 */

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(path="/order")
    public String showOrder(Model model) {

        return "order";
    }

    @PostMapping(path="/delete-order/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, Model model, HttpServletRequest request) {

        orderRepository.delete(orderId);
        //CHECK WHETHER RETURNS ONE ORDER OR LIST BEFORE CASTING
        List<Order> orders = (List<Order>)request.getSession().getAttribute("orders");
        for(Order order:orders) {
            if(order.getOrderId() == orderId) {
                orders.remove(order);
                break;
            }
        }
        model.addAttribute("orders", orders);
        return "order";
    }

    @GetMapping(path="/order-edit/{orderId}")
    public String showEditView(@PathVariable("orderId") Long orderId, Model model) {

        model.addAttribute("order", orderRepository.findOne(orderId));
        return "order-edit";
    }

    @PostMapping(path="/update-order/{orderId}")
    public String updateOrder(@PathVariable("orderId") Long orderId, @RequestParam(value="customerName") String customerName,
                              @RequestParam(value="customerAddress") String customerAddress,
                              @RequestParam(value="orderReference") Integer orderReference,
                              @RequestParam(value="datePlaced") String datePlaced, @RequestParam(value="status") String status,
                              Model model, HttpServletRequest request) {

        Order order = orderRepository.findOne(orderId);
        order.setCustomerName(customerName);
        order.setCustomerAddress(customerAddress);
        order.setOrderReference(orderReference);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(datePlaced);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        order.setDate(date);
        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);

        if(request.getSession().getAttribute("orders").getClass().toString().equals("class demo.model.Order")) {
            model.addAttribute("orders", order);
        } else {
            List<Order> orders = (List<Order>)request.getSession().getAttribute("orders");
            for(Order ord:orders) {
                if(ord.getOrderId().equals(orderId)) {
                    orders.remove(ord);
                    break;
                }
            }
            orders.add(0, order);
            model.addAttribute("orders", orders);
        }
        return "order";
    }

    @GetMapping(path="/get-order")
    public String getOrderByIndividualParameter(@RequestParam(value="orderId", required=false, defaultValue="") String orderId,
                               @RequestParam(value="customerName", required=false, defaultValue="") String customerName,
                               @RequestParam(value="customerAddress", required=false, defaultValue="") String customerAddress,
                               @RequestParam(value="orderReference", required=false, defaultValue="") String orderReference,
                               @RequestParam(value="datePlaced", required=false, defaultValue="") String datePlaced,
                               @RequestParam(value="status", required=false, defaultValue="") String status,
                               Model model, HttpServletRequest request){

        List<Order> orders;
        List<Product> products =  null;

        if(!orderId.equals("")) {
            Order order = orderRepository.findOne(Long.parseLong(orderId));
            Iterator<OrderLine> orderLines = order.getLines().iterator();
            while(orderLines.hasNext()){
                products.add(orderLines.next().getProduct());
            }
            model.addAttribute("products", products);
            model.addAttribute("orders", order);
            request.getSession().setAttribute("orders", order);
            request.getSession().setAttribute("products", products);
        }
        if(!customerName.equals("")) {
            orders = orderRepository.findByCustomerName(customerName);
            //display product set for every order
            //List<List<Product>> ?? Iterate and display product IDs if not possible with modal
            int i = 1;
            for(Order order : orders) {
                Iterator<OrderLine> orderLines = order.getLines().iterator();
                while(orderLines.hasNext()){
                    products.add(orderLines.next().getProduct());
                }
                model.addAttribute("products"+i, products);
                i++;
            }
            //display product set for every order
            model.addAttribute("orders", orders);
            request.getSession().setAttribute("orders", orders);
        }
        if(!customerAddress.equals("")) {
            orders = orderRepository.findByCustomerAddress(customerAddress);
            model.addAttribute("orders", orders);
            request.getSession().setAttribute("orders", orders);
        }
        if(!orderReference.equals("")) {
            System.out.println(orderReference);
            orders = orderRepository.findByOrderReference(Integer.parseInt(orderReference));
            System.out.println(orders.get(0).getOrderId());
            model.addAttribute("orders", orders);
            request.getSession().setAttribute("orders", orders);
        }
        if(!datePlaced.equals("")) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatter.parse(datePlaced);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            orders = orderRepository.findByDate(date);
            model.addAttribute("orders", orders);
            request.getSession().setAttribute("orders", orders);
        }
        if(!status.equals("")) {

            orders = orderRepository.findByStatus(OrderStatus.valueOf(status));
            model.addAttribute("orders", orders);
            request.getSession().setAttribute("orders", orders);
        }
        return "order";
    }

}
