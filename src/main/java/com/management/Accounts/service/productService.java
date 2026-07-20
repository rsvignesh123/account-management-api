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

    @Autowired
    private NotificationService notificationService;

    public productModel saveProduct(productModel product)
    {
        productModel savedProduct = repo.save(product);
        notificationService.saveNotification(
                "New Product",
                savedProduct.getProductName() + " product added successfully.",
                "PRODUCT",
                "CREATE",
                savedProduct.getId()
        );

        return  savedProduct;
    }
    public List<productModel> getAllProducts() {
        return repo.findAll();
    }
    public productModel getById(String id) {
        return repo.findById(id).orElse(null);
    }

    // Delete
    public void deleteCustomer(String id) {
        productModel product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        repo.deleteById(id);

        notificationService.saveNotification(
                "Product Deleted",
                product.getProductName() + " product deleted.",
                "PRODUCT",
                "DELETE",
                product.getId()
        );
    }
    public productModel findByProductName(String productName) {
        System.out.println("Searching : " + productName);

        System.out.println("All Products : " + repo.findAll());
        return repo.findByProductName(productName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public productModel updateProduct(String id, productModel product) {
        product.setId(id);
        productModel updatedProduct = repo.save(product);

        notificationService.saveNotification(
                "Product Updated",
                updatedProduct.getProductName() + " product updated.",
                "PRODUCT",
                "UPDATE",
                updatedProduct.getId()
        );
        return updatedProduct;
    }

}
