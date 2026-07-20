package com.management.Accounts.controller;

import com.management.Accounts.entity.orderStoreModel;
import com.management.Accounts.service.orderStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/storeOrders")
public class orderStoreController {


    @Autowired
    private orderStoreService orderService;

    @PostMapping
    public orderStoreModel createOrder(@RequestBody orderStoreModel request) {
        LocalDateTime now = LocalDateTime.now();

        request.setCreatedAt(now);
        request.setUpdatedAt(now);

       return orderService.saveStoreOrder(request);

    }
    @GetMapping
    public List<orderStoreModel> getOrder()
    {
        return orderService.getAllStoreOrder();
    }
    @GetMapping("/{id}")
    public orderStoreModel getById(@PathVariable String id) {
        return orderService.getById(id);
    }
    @PutMapping("/{id}")
    public orderStoreModel updateStoreOrder(@PathVariable String id, @RequestBody orderStoreModel mango)
    {

        return orderService.updateStoreOrder(id,mango);
    }
    @DeleteMapping("/{id}")
    public void deleteStoreOrder(@PathVariable String id)
    {
        orderService.deleteStoreOrder(id);
    }
    @PatchMapping("/{id}/status")
    public orderStoreModel updateStatus(
            @PathVariable String id,
            @RequestParam String status) {

        return orderService.updateStatus(id, status);
    }
    @GetMapping("/search")
    public List<orderStoreModel> searchOrders(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate orderDate,
            @RequestParam(required = false) String orderStatus) {

        return orderService.searchOrders(companyName,orderDate,orderStatus);
    }
        @GetMapping("/searchRange")
        public List<orderStoreModel> searchOrdersRange(
                @RequestParam(required = false) String companyName,
                @RequestParam(required = false) LocalDate orderStartDate,
                @RequestParam(required = false) LocalDate orderEndDate,
                @RequestParam(required = false) String orderStatus) {
    
            return orderService.searchOrdersRange(companyName,orderStartDate,orderEndDate,orderStatus);
        }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable String id){

        byte[] pdf = orderService.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Order_Report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
   }
