
package com.management.Accounts.controller;


import com.management.Accounts.entity.productModel;
import com.management.Accounts.service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")    
public class  productController {

    @Autowired
    private productService service;

    @PostMapping
    public productModel create(@RequestBody productModel product)
    {
        return service.saveProduct(product);
    }

    @GetMapping
    public List<productModel> getAll() {
        return service.getAllProducts();
    }
    @GetMapping("/{id}")
    public productModel getById(@PathVariable String id) {
        return service.getById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {

        productModel product = service.getById(id);

        if (product == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Customer not found");
        }

        service.deleteCustomer(id);

        return ResponseEntity.ok("Deleted Successfully");
    }
    @PutMapping("/{id}")
    public productModel updateCustomer(@PathVariable String id, @RequestBody productModel product)
    {
        System.out.println(id);
        return service.updateProduct(id,product);
    }
    @GetMapping("/product/{productName}")
    public productModel getCustomerByCompanyName(@PathVariable String productName) {
        return service.findByProductName(productName);
    }
}
