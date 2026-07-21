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
    public customerModel saveCustomer(customerModel customer) {

        customerModel savedCustomer = repo.save(customer);

        notificationService.saveNotification(
                "New Customer",
                savedCustomer.getCompanyName() + " customer added successfully.",
                "CUSTOMER",
                "CREATE",
                savedCustomer.getId(),
                savedCustomer.getTenantId()
        );

        return savedCustomer;
    }
    public List<customerModel> getAllCustomers(String tenantId) {
        return repo.findByTenantId(tenantId);
    }
    public customerModel getById(String id, String tenantId) {

        return repo.findByIdAndTenantId(id, tenantId)
                .orElse(null);
    }

    // Delete
    public void deleteCustomer(String id, String tenantId) {

        customerModel customer = repo.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        repo.delete(customer);

        notificationService.saveNotification(
                "Customer Deleted",
                customer.getCompanyName() + " customer deleted.",
                "CUSTOMER",
                "DELETE",
                customer.getId(),
                tenantId
        );
    }
    public customerModel updateCustomer(
            String id,
            customerModel customer,
            String tenantId) {

        customerModel existing = repo.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        existing.setCustomerName(customer.getCustomerName());
        existing.setCompanyName(customer.getCompanyName());
        existing.setMobilenumber(customer.getMobilenumber());
        existing.setCity(customer.getCity());
        existing.setType(customer.getType());
        existing.setLatitude(customer.getLatitude());
        existing.setLongitude(customer.getLongitude());



        customerModel updatedCustomer = repo.save(existing);

        notificationService.saveNotification(
                "Customer Updated",
                updatedCustomer.getCompanyName() + " customer updated.",
                "CUSTOMER",
                "UPDATE",
                updatedCustomer.getId(),
                tenantId
        );

        return updatedCustomer;
    }
    public customerModel findByCompanyName(
            String companyName,
            String tenantId) {

        System.out.println("Searching : " + companyName);
        System.out.println("Tenant : " + tenantId);

        return repo.findByCompanyNameAndTenantId(
                companyName,
                tenantId
        ).orElseThrow(() ->
                new RuntimeException("Customer not found"));
    }
    public List<customerModel> optimize(List<customerModel> customers){

        customers.sort(
                Comparator.comparing(customerModel::getLatitude)
                        .thenComparing(customerModel::getLongitude)
        );

        return customers;

    }
}
