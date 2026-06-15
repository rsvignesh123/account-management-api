package com.management.Accounts.repository;


import com.management.Accounts.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface employeeRepository
        extends MongoRepository<Employee, String> {
}
