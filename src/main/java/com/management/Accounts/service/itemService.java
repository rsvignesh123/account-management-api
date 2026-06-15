package com.management.Accounts.service;

import com.management.Accounts.entity.customerModel;
import com.management.Accounts.entity.itemModel;
import com.management.Accounts.entity.orderModel;
import com.management.Accounts.repository.itemRepository;
import com.management.Accounts.repository.orderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class itemService {
    @Autowired
    private itemRepository repo;

    public itemModel saveItem(itemModel item)
    {
        return repo.save(item);
    }
    public List<itemModel> getItemsByOrderId(String orderId)
    {
        return repo.findByOrderId(orderId);
    }
    public List<itemModel> getAllItems() {
        return repo.findAll();
    }
    public itemModel getItemById(String id) {
        return repo.findById(id).orElse(null);
    }
    public void deleteItem(String id) {
        repo.deleteById(id);
    }
    public itemModel updateItems(String id, itemModel mango) {
        mango.setId(id);
        return repo.save(mango);
    }
}
