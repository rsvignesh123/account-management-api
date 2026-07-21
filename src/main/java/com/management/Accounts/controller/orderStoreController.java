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
    public orderStoreModel createOrder(
            @RequestBody orderStoreModel request,
            @RequestHeader("tenantId") String tenantId
    ) {

        LocalDateTime now = LocalDateTime.now();

        request.setTenantId(tenantId);
        request.setCreatedAt(now);
        request.setUpdatedAt(now);

        return orderService.saveStoreOrder(request);
    }



    @GetMapping
    public List<orderStoreModel> getOrder(
            @RequestHeader("tenantId") String tenantId
    ) {

        return orderService.getAllStoreOrder(tenantId);
    }



    @GetMapping("/{id}")
    public orderStoreModel getById(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId
    ) {

        return orderService.getById(id, tenantId);
    }



    @PutMapping("/{id}")
    public orderStoreModel updateStoreOrder(
            @PathVariable String id,
            @RequestBody orderStoreModel mango,
            @RequestHeader("tenantId") String tenantId
    ) {

        return orderService.updateStoreOrder(
                id,
                mango,
                tenantId
        );
    }



    @DeleteMapping("/{id}")
    public void deleteStoreOrder(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId
    ) {

        orderService.deleteStoreOrder(id, tenantId);
    }



    @PatchMapping("/{id}/status")
    public orderStoreModel updateStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestHeader("tenantId") String tenantId
    ) {

        return orderService.updateStatus(
                id,
                status,
                tenantId
        );
    }




    @GetMapping("/search")
    public List<orderStoreModel> searchOrders(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate orderDate,
            @RequestParam(required = false) String orderStatus,
            @RequestHeader("tenantId") String tenantId
    ) {

        return orderService.searchOrders(
                companyName,
                orderDate,
                orderStatus,
                tenantId
        );
    }





    @GetMapping("/searchRange")
    public List<orderStoreModel> searchOrdersRange(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate orderStartDate,
            @RequestParam(required = false) LocalDate orderEndDate,
            @RequestParam(required = false) String orderStatus,
            @RequestHeader("tenantId") String tenantId
    ) {

        return orderService.searchOrdersRange(
                companyName,
                orderStartDate,
                orderEndDate,
                orderStatus,
                tenantId
        );
    }




    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId
    ){

        byte[] pdf =
                orderService.generatePdf(id, tenantId);


        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"Order_Report.pdf\""
                )
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_PDF_VALUE
                )
                .body(pdf);
    }

}
