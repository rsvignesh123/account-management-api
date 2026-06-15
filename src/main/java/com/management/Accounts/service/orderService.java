package com.management.Accounts.service;

import com.management.Accounts.entity.itemModel;
import com.management.Accounts.entity.orderModel;
import com.management.Accounts.repository.orderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class orderService {
    @Autowired
    private orderRepository repo;

    public orderModel saveOrder(orderModel order) {
        System.out.println(order.getOrderDate() + "order");
        return repo.save(order);
    }

    public List<orderModel> getAllOrders() {
        return repo.findAllByOrderByOrderDateDesc();
    }

    public orderModel getOrderById(String id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteOrder(String id) {
        repo.deleteById(id);
    }

    public orderModel updateOrder(String id, orderModel newData) {
        orderModel existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setOrderDate(newData.getOrderDate());
            existing.setOrderCompanyName(newData.getOrderCompanyName());
            existing.setPaidAmount(newData.getPaidAmount());
            existing.setBillAmount(newData.getBillAmount());
            existing.setBalanceAmount(newData.getBalanceAmount());

            return repo.save(existing);
        }
        return null;
    }

    public orderModel updateBillAmount(String orderId, long amount) {
        orderModel order = repo.findById(orderId).orElse(null);

        if (order != null) {
            order.setBillAmount(amount);
            return repo.save(order);
        }
        return null;
    }

    public List<orderModel> searchOrders(String companyName, LocalDate orderDate) {


        boolean hasCompany =
                companyName != null && !companyName.trim().isEmpty();

        boolean hasDate =
                orderDate != null;
        if (hasCompany && hasDate) {
            return repo
                    .findByOrderCompanyNameContainingIgnoreCaseAndOrderDateOrderByOrderDateDesc(
                            companyName,
                            orderDate);
        }
        if (hasCompany) {

            return repo
                    .findByOrderCompanyNameContainingIgnoreCaseOrderByOrderDateDesc(
                            companyName);

        }
        if (hasDate) {

            return repo
                    .findByOrderDateOrderByOrderDateDesc(
                            orderDate);

        }
        return repo.findAllByOrderByOrderDateDesc();
    }

    public List<orderModel> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {

        System.out.println("Start = " + startDate);
        System.out.println("End = " + endDate);

        List<orderModel> orders = repo.findByOrderDateBetween(startDate, endDate);
        System.out.println("Orders = " + orders);
        System.out.println("Count = " + orders.size());

        return orders;
    }
}