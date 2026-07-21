package com.management.Accounts.controller;


import com.management.Accounts.entity.customerModel;
import com.management.Accounts.service.customerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class customerController {

    @Autowired
    private customerService service;

    @PostMapping
    public customerModel create(
            @RequestBody customerModel customer,
            @RequestHeader("tenantId") String tenantId
    ) {

        customer.setTenantId(tenantId);

        return service.saveCustomer(customer);
    }

    @GetMapping
    public List<customerModel> getAll(@RequestHeader("tenantId") String tenantId) {
        return service.getAllCustomers(tenantId);
    }
    @GetMapping("/{id}")
    public customerModel getById(@PathVariable String id,
                                 @RequestHeader("tenantId") String tenantId) {
        return service.getById(id,tenantId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id,@RequestHeader("tenantId") String tenantId) {

        customerModel customer = service.getById(id,tenantId);

        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Customer not found");
        }

        service.deleteCustomer(id,tenantId);

        return ResponseEntity.ok("Deleted Successfully");
    }
    @PutMapping("/{id}")
    public customerModel updateCustomer(
            @PathVariable String id,
            @RequestBody customerModel customer,
            @RequestHeader("tenantId") String tenantId
    ) {

        return service.updateCustomer(id, customer, tenantId);
    }
    @GetMapping("/company/{companyName}")
    public customerModel getCustomerByCompanyName(
            @PathVariable String companyName,
            @RequestHeader("tenantId") String tenantId) {

        return service.findByCompanyName(companyName, tenantId);
    }
    @PostMapping("/optimize-route")
    public List<customerModel> optimizeRoute(
            @RequestBody List<customerModel> customers){

        return service.optimize(customers);

    }
}
