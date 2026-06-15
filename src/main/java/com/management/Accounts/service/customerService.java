package com.management.Accounts.service;


import com.management.Accounts.entity.customerModel;
import com.management.Accounts.repository.customerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class customerService {

    @Autowired
    private customerRepository repo;

    public customerModel saveCustomer(customerModel mango)
    {
        return  repo.save(mango);
    }
    public List<customerModel> getAllCustomers() {
        return repo.findAll();
    }
    public customerModel getById(String id) {
        return repo.findById(id).orElse(null);
    }

    // Delete
    public void deleteCustomer(String id) {
        repo.deleteById(id);
    }
    public customerModel updateCustomer(String id, customerModel mango) {
        mango.setId(id);
        return repo.save(mango);
    }
}
