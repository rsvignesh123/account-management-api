package com.management.Accounts.service;


import com.management.Accounts.entity.productModel;
import com.management.Accounts.repository.productRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class productService {

    @Autowired
    private productRepository repo;

    public productModel saveProduct(productModel data)
    {
        return  repo.save(data);
    }
    public List<productModel> getAllProducts() {
        return repo.findAll();
    }
    public productModel getById(String id) {
        return repo.findById(id).orElse(null);
    }

    // Delete
    public void deleteCustomer(String id) {
        repo.deleteById(id);
    }
    public productModel updateProduct(String id, productModel data) {
        data.setId(id);
        return repo.save(data);
    }
}
