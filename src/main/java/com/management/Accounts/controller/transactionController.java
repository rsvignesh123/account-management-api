package com.management.Accounts.controller;

import com.management.Accounts.entity.transactionModel;
import com.management.Accounts.service.transactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class transactionController {

    @Autowired
    transactionService service;
    @PostMapping
    public transactionModel saveTransactionData(@RequestBody transactionModel transactionData)
    {
        LocalDateTime now = LocalDateTime.now();

        transactionData.setCreatedAt(now);
        transactionData.setUpdatedAt(now);
        return  service.saveTransactionData(transactionData);
    }
    @GetMapping
    public List<transactionModel> getAllTransactionDetails()
    {
        return service.getAllTransaction();
    }
    @PutMapping("/{id}")
    public transactionModel updateTransaction(@PathVariable String id, @RequestBody transactionModel transaction)
    {


        return service.updateTransaction(id,transaction);
    }
    @GetMapping("/search")
    public List<transactionModel> searchOrders(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate transactionDate,
            @RequestParam(required = false) String collectedPerson) {

        return service.searchTransactions(companyName,transactionDate,collectedPerson);
    }
    @GetMapping("/searchRange")
    public List<transactionModel> searchOrdersRange(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate transactionStartDate,
            @RequestParam(required = false) LocalDate transactionEndDate,
            @RequestParam(required = false) String collectedPerson) {

        return service.searchTransactionsRange(companyName,transactionStartDate,transactionEndDate,collectedPerson);
    }
    @DeleteMapping("/{id}")
    public void deleteStoreOrder(@PathVariable String id)
    {
        service.deleteTransaction(id);
    }
}
