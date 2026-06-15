package com.management.Accounts.controller;

import com.management.Accounts.entity.customerModel;
import com.management.Accounts.entity.itemModel;
import com.management.Accounts.entity.orderModel;
import com.management.Accounts.service.itemService;
import com.management.Accounts.service.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/items")
public class itemController {
    @Autowired
    private itemService service;

    @PostMapping
    public itemModel saveItem(@RequestBody itemModel item) {

        return service.saveItem(item);
    }
    @GetMapping
    public List<itemModel> getAllItems() {
        return service.getAllItems();
    }

    @GetMapping("/{id}")
    public itemModel getOrderById(@PathVariable String id) {
        return service.getItemById(id);
    }
    @GetMapping("/order/{orderId}")
    public List<itemModel> getItemsByOrderId(@PathVariable String orderId)
    {
        return service.getItemsByOrderId(orderId);
    }
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        service.deleteItem(id);
    }
    @PutMapping("/{id}")
    public itemModel updateCustomer(@PathVariable String id, @RequestBody itemModel mango)
    {
        System.out.println(id);
        return service.updateItems(id,mango);
    }
}
