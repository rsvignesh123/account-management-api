package com.management.Accounts.service;

import com.management.Accounts.DTO.OrderItemRequest;
import com.management.Accounts.entity.orderStoreModel;
import com.management.Accounts.repository.orderStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class orderStoreService {
    @Autowired
    orderStoreRepository repository;
    public orderStoreModel saveStoreOrder(orderStoreModel request) {

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.getItems()) {
            total = total.add(BigDecimal.valueOf(item.getPrice()));
        }

        request.setTotalAmount(total);

        return repository.save(request);
    }
}
