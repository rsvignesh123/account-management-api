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
public class productController {


    @Autowired
    private productService service;



    @PostMapping
    public productModel create(
            @RequestBody productModel product,
            @RequestHeader("tenantId") String tenantId
    ) {

        product.setTenantId(tenantId);

        return service.saveProduct(product);
    }




    @GetMapping
    public List<productModel> getAll(
            @RequestHeader("tenantId") String tenantId
    ) {

        return service.getAllProducts(tenantId);
    }





    @GetMapping("/{id}")
    public productModel getById(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId
    ) {

        return service.getById(id, tenantId);
    }






    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId
    ) {


        productModel product =
                service.getById(id, tenantId);


        if(product == null){

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }


        service.deleteProduct(id, tenantId);


        return ResponseEntity.ok(
                "Deleted Successfully"
        );
    }





    @PutMapping("/{id}")
    public productModel update(
            @PathVariable String id,
            @RequestBody productModel product,
            @RequestHeader("tenantId") String tenantId
    ) {


        return service.updateProduct(
                id,
                product,
                tenantId
        );
    }





    @GetMapping("/product/{productName}")
    public productModel getByProductName(
            @PathVariable String productName,
            @RequestHeader("tenantId") String tenantId
    ) {


        return service.findByProductName(
                productName,
                tenantId
        );
    }

}