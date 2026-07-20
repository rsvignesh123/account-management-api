package com.management.Accounts.service;

import com.management.Accounts.entity.customerModel;
import com.management.Accounts.repository.customerRepository;
import com.management.Accounts.repository.orderStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class scheduleService {
    @Autowired
    private orderStoreRepository orderRepo;

    @Autowired
    private customerRepository customerRepo;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReminder() {

        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);

        List<String> inactiveCompanies = new ArrayList<>();

        List<customerModel> customers = customerRepo.findAll();

        for (customerModel customer : customers) {

            boolean hasRecentOrder = orderRepo.existsByCompanyNameAndOrderDateAfter(
                    customer.getCompanyName(),
                    tenDaysAgo
            );

            if (!hasRecentOrder) {
                inactiveCompanies.add(customer.getCompanyName());
            }
        }

        if (!inactiveCompanies.isEmpty()) {
            emailService.sendReminderMail(inactiveCompanies);
        }
    }

}
