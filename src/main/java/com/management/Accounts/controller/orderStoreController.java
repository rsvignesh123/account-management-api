package com.management.Accounts.controller;

import com.management.Accounts.entity.orderStoreModel;
import com.management.Accounts.service.orderStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/storeOrders")
public class orderStoreController {


    @Autowired
    private orderStoreService orderService;

    @PostMapping
    public orderStoreModel createOrder(@RequestBody orderStoreModel request) {

       return orderService.saveStoreOrder(request);

    }
}
