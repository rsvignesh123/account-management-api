package com.management.Accounts.service;



import com.management.Accounts.entity.transactionModel;
import com.management.Accounts.repository.transactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class transactionService {

    @Autowired
    transactionRepository repo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NotificationService notificationService;
    public transactionModel saveTransactionData(transactionModel transaction) {
        transactionModel data=repo.save(transaction);
        transaction.setTransactionTime(LocalTime.now());
        notificationService.saveNotification(
                "New Transaction",
                data.getCompanyName() + "paid" + data.getAmount() + "successfully",
                "TRANSACTION",
                "CREATE",
                data.getId()
        );
        return data;
    }

    public List<transactionModel> getAllTransaction() {
        return repo.findAll();
    }

    public transactionModel updateTransaction(String id, transactionModel data) {
        transactionModel old = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        old.setId(id);   // <-- Add this

        old.setCompanyName(data.getCompanyName());
        old.setTransactionMode(data.getTransactionMode());
        old.setTransactionDate(data.getTransactionDate());
        old.setTransactionTime(data.getTransactionTime());
        old.setAmount(data.getAmount());
        old.setCollectedPerson(data.getCollectedPerson());
        old.setRemarks(data.getRemarks());
        transactionModel transaction=repo.save(old);
        old.setUpdatedAt(LocalDateTime.now());
        notificationService.saveNotification(
                "Update Transaction",
                "transaction updated successfully",
                "TRANSACTION",
                "CREATE",
                data.getId()
        );
        return transaction;
    }

    public List<transactionModel> searchTransactions(
            String companyName,
            LocalDate transactionDate,
            String collectedPerson) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // Company Name
        if (companyName != null && !companyName.trim().isEmpty()) {
            criteriaList.add(
                    Criteria.where("companyName")
                            .regex(companyName, "i") // Ignore case
            );
        }

        // Transaction Date
        if (transactionDate != null) {
            criteriaList.add(
                    Criteria.where("transactionDate")
                            .is(transactionDate)
            );
        }

        // Collected Person
        if (collectedPerson != null && !collectedPerson.trim().isEmpty()) {
            criteriaList.add(
                    Criteria.where("collectedPerson")
                            .is(collectedPerson)
            );
        }

        // Combine all filters
        if (!criteriaList.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(criteriaList.toArray(new Criteria[0]))
            );
        }

        // Sorting
        query.with(Sort.by(Sort.Direction.DESC, "transactionDate"));

        return mongoTemplate.find(query, transactionModel.class);
    }
    public List<transactionModel> searchTransactionsRange(
            String companyName,
            LocalDate transactionStartDate,
            LocalDate transactionEndDate,
            String collectedPerson) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // Company Name
        if (companyName != null && !companyName.trim().isEmpty()) {
            criteriaList.add(
                    Criteria.where("companyName")
                            .regex(companyName, "i")
            );
        }

        // Transaction Date Range
        if (transactionStartDate != null && transactionEndDate != null) {
            criteriaList.add(
                    Criteria.where("transactionDate")
                            .gte(transactionStartDate)
                            .lte(transactionEndDate)
            );
        }
        // Start Date Only
        else if (transactionStartDate != null) {
            criteriaList.add(
                    Criteria.where("transactionDate")
                            .is(transactionStartDate)
            );
        }
        // End Date Only
        else if (transactionEndDate != null) {
            criteriaList.add(
                    Criteria.where("transactionDate")
                            .is(transactionEndDate)
            );
        }

        // Collected Person
        if (collectedPerson != null && !collectedPerson.trim().isEmpty()) {
            criteriaList.add(
                    Criteria.where("collectedPerson")
                            .is(collectedPerson)
            );
        }

        // Apply all filters
        if (!criteriaList.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(criteriaList.toArray(new Criteria[0]))
            );
        }

        // Sorting
        query.with(Sort.by(Sort.Direction.DESC, "transactionDate"));

        return mongoTemplate.find(query, transactionModel.class);
    }
    public void deleteTransaction(String id) {
        transactionModel transaction = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        repo.deleteById(id);
        notificationService.saveNotification(
                "Transaction Deleted",
                " Transaction deleted.",
                "TRANSACTION",
                "DELETE",
                transaction.getId()
        );

    }
}