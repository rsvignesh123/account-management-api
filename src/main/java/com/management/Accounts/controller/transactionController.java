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
    public transactionModel saveTransactionData(
            @RequestBody transactionModel transactionData,
            @RequestHeader("tenantId") String tenantId
    ){

        LocalDateTime now = LocalDateTime.now();

        transactionData.setCreatedAt(now);
        transactionData.setUpdatedAt(now);

        return service.saveTransactionData(
                transactionData,
                tenantId
        );
    }



    @GetMapping
    public List<transactionModel> getAllTransactionDetails(
            @RequestHeader("tenantId") String tenantId
    ){

        return service.getAllTransaction(
                tenantId
        );
    }



    @PutMapping("/{id}")
    public transactionModel updateTransaction(
            @PathVariable String id,
            @RequestBody transactionModel transaction,
            @RequestHeader("tenantId") String tenantId
    ){

        return service.updateTransaction(
                id,
                transaction,
                tenantId
        );
    }



    @GetMapping("/search")
    public List<transactionModel> searchOrders(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate transactionDate,
            @RequestParam(required = false) String collectedPerson,
            @RequestHeader("tenantId") String tenantId
    ){

        return service.searchTransactions(
                companyName,
                transactionDate,
                collectedPerson,
                tenantId
        );
    }



    @GetMapping("/searchRange")
    public List<transactionModel> searchOrdersRange(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) LocalDate transactionStartDate,
            @RequestParam(required = false) LocalDate transactionEndDate,
            @RequestParam(required = false) String collectedPerson,
            @RequestHeader("tenantId") String tenantId
    ){

        return service.searchTransactionsRange(
                companyName,
                transactionStartDate,
                transactionEndDate,
                collectedPerson,
                tenantId
        );
    }



    @DeleteMapping("/{id}")
    public void deleteStoreOrder(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId
    ){

        service.deleteTransaction(
                id,
                tenantId
        );
    }
}
