package com.management.Accounts.service;


import com.management.Accounts.entity.customerModel;
import com.management.Accounts.repository.customerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class customerService {

    @Autowired
    private customerRepository repo;
    @Autowired
    private NotificationService notificationService;
    public customerModel saveCustomer(customerModel customer)
    {
        customerModel savedCustomer = repo.save(customer);
        notificationService.saveNotification(
                "New Customer",
                savedCustomer.getCompanyName() + " customer added successfully.",
                "CUSTOMER",
                "CREATE",
                savedCustomer.getId()
        );
        return  savedCustomer;
    }
    public List<customerModel> getAllCustomers() {
        return repo.findAll();
    }
    public customerModel getById(String id) {
        return repo.findById(id).orElse(null);
    }

    // Delete
    public void deleteCustomer(String id) {
        customerModel customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        repo.deleteById(id);

        notificationService.saveNotification(
                "Customer Deleted",
                customer.getCompanyName() + " customer deleted.",
                "CUSTOMER",
                "DELETE",
                customer.getId()
        );
    }
    public customerModel updateCustomer(String id, customerModel customer) {
        customer.setId(id);

        customerModel updatedCustomer = repo.save(customer);

        notificationService.saveNotification(
                "Customer Updated",
                updatedCustomer.getCompanyName() + " customer updated.",
                "CUSTOMER",
                "UPDATE",
                updatedCustomer.getId()
        );

        return updatedCustomer;
    }
    public customerModel findByCompanyName(String companyName) {
        System.out.println("Searching : " + companyName);

        System.out.println("All Customers : " + repo.findAll());
        return repo.findByCompanyName(companyName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
    public List<customerModel> optimize(List<customerModel> customers){

        customers.sort(
                Comparator.comparing(customerModel::getLatitude)
                        .thenComparing(customerModel::getLongitude)
        );

        return customers;

    }
}
