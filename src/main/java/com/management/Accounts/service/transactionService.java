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
    public transactionModel saveTransactionData(
            transactionModel transaction,
            String tenantId
    ) {

        transaction.setTenantId(tenantId);
        transaction.setTransactionTime(LocalTime.now());

        transactionModel data =
                repo.save(transaction);


        notificationService.saveNotification(
                "New Transaction",
                data.getCompanyName()
                        + " paid "
                        + data.getAmount()
                        + " successfully",
                "TRANSACTION",
                "CREATE",
                data.getId(),
                tenantId
        );


        return data;
    }

    public List<transactionModel> getAllTransaction(
            String tenantId
    ) {

        return repo.findByTenantId(tenantId);

    }

    public transactionModel updateTransaction(
            String id,
            transactionModel data,
            String tenantId
    ) {


        transactionModel old =
                repo.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(
                                () -> new RuntimeException("Transaction not found")
                        );


        old.setCompanyName(data.getCompanyName());
        old.setTransactionMode(data.getTransactionMode());
        old.setTransactionDate(data.getTransactionDate());
        old.setTransactionTime(data.getTransactionTime());
        old.setAmount(data.getAmount());
        old.setCollectedPerson(data.getCollectedPerson());
        old.setRemarks(data.getRemarks());
        old.setUpdatedAt(LocalDateTime.now());


        transactionModel updated =
                repo.save(old);



        notificationService.saveNotification(
                "Update Transaction",
                "transaction updated successfully",
                "TRANSACTION",
                "UPDATE",
                updated.getId(),
                tenantId
        );


        return updated;

    }

    public List<transactionModel> searchTransactions(
            String companyName,
            LocalDate transactionDate,
            String collectedPerson,
            String tenantId) {

        Query query = new Query();

        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(
                Criteria.where("tenantId")
                        .is(tenantId)
        );
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
            String collectedPerson,
            String tenantId) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(
                Criteria.where("tenantId")
                        .is(tenantId)
        );
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
    public void deleteTransaction(
            String id,
            String tenantId
    ) {


        transactionModel transaction =
                repo.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(
                                () -> new RuntimeException("Transaction not found")
                        );


        repo.delete(transaction);


        notificationService.saveNotification(
                "Transaction Deleted",
                "Transaction deleted.",
                "TRANSACTION",
                "DELETE",
                transaction.getId(),
                tenantId
        );

    }
}