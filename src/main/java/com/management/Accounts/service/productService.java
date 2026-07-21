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

    public productModel saveProduct(
            productModel product
    ) {

        productModel savedProduct =
                repo.save(product);


        notificationService.saveNotification(
                "New Product",
                savedProduct.getProductName()
                        + " product added successfully.",
                "PRODUCT",
                "CREATE",
                savedProduct.getId(),
                savedProduct.getTenantId()
        );


        return savedProduct;
    }
    public List<productModel> getAllProducts(
            String tenantId
    ) {

        return repo.findByTenantId(tenantId);

    }
    public productModel getById(
            String id,
            String tenantId
    ) {

        return repo.findByIdAndTenantId(id, tenantId)
                .orElse(null);

    }

    // Delete
    public void deleteProduct(
            String id,
            String tenantId
    ) {


        productModel product =
                repo.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(
                                () -> new RuntimeException("Product not found")
                        );


        repo.delete(product);



        notificationService.saveNotification(
                "Product Deleted",
                product.getProductName()
                        + " product deleted.",
                "PRODUCT",
                "DELETE",
                product.getId(),
                tenantId
        );

    }

    public productModel findByProductName(
            String productName,
            String tenantId
    ) {


        return repo.findByProductNameAndTenantId(
                        productName,
                        tenantId
                )
                .orElseThrow(
                        () -> new RuntimeException("Product not found")
                );

    }
    public productModel updateProduct(
            String id,
            productModel product,
            String tenantId
    ) {


        productModel existing =
                repo.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(
                                () -> new RuntimeException("Product not found")
                        );


        existing.setProductName(product.getProductName());
        existing.setPrice(product.getPrice());



        productModel updatedProduct =
                repo.save(existing);



        notificationService.saveNotification(
                "Product Updated",
                updatedProduct.getProductName()
                        + " product updated.",
                "PRODUCT",
                "UPDATE",
                updatedProduct.getId(),
                tenantId
        );


        return updatedProduct;

    }



}
