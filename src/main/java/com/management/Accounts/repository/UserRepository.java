package com.management.Accounts.repository;


import com.management.Accounts.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {


    Optional<User> findByUsername(String username);


    Optional<User> findByUsernameAndTenantId(
            String username,
            String tenantId
    );


    Optional<User> findByTenantId(
            String tenantId
    );
}