package com.management.Accounts.controller;

import com.management.Accounts.entity.customerModel;
import com.management.Accounts.entity.orderModel;
import com.management.Accounts.service.EmailService;
import com.management.Accounts.service.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class orderController {

    @Autowired
    private orderService service;
    @Autowired
    private EmailService emailService;
    @PostMapping
    public orderModel saveOrder(@RequestBody orderModel order) {
        System.out.println(order.getOrderDate() +"order f");
        System.out.println(order.getOrderDate().getClass());
//        order.setOrderDate(LocalDate.now(ZoneId.of("Asia/Kolkata")));
        return service.saveOrder(order);
    }
    @GetMapping
    public List<orderModel> getAllOrders() {    
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public orderModel getOrderById(@PathVariable String id) {
        return service.getOrderById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        service.deleteOrder(id);
    }
    @PutMapping("/{id}")
    public orderModel updateCustomer(@PathVariable String id, @RequestBody orderModel mango)
    {
        System.out.println(id);
        return service.updateOrder(id,mango);
    }
    @PutMapping("/{id}/billAmount")
    public orderModel updateBillAmount(
            @PathVariable String id,
            @RequestParam long amount) {

        return service.updateBillAmount(id, amount);
    }
    @GetMapping("/search")
    public List<orderModel> searchOrders(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate orderDate) {

        return service.searchOrders(companyName,orderDate);
    }
    @GetMapping("/test-mail")
    public String testMail() {

        emailService.sendReminderMail(
                List.of("Arumai Masala")
        );

        return "Mail Sent";
    }
    @GetMapping("/filter")
    public List<orderModel> getOrderByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return service.getOrdersByDateRange(startDate, endDate);
    }

}
