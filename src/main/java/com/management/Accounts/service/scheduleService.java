package com.management.Accounts.service;

import com.management.Accounts.entity.companyProfileModel;
import com.management.Accounts.entity.customerModel;
import com.management.Accounts.repository.companyProfileRepository;
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

    @Autowired
    private companyProfileRepository companyRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReminder() {


        LocalDate tenDaysAgo =
                LocalDate.now().minusDays(10);



        List<String> tenants =
                orderRepo.findDistinctTenantIdBy();



        for(String tenantId : tenants){


            List<String> inactiveCompanies =
                    new ArrayList<>();


            List<customerModel> customers =
                    customerRepo.findByTenantId(tenantId);



            for(customerModel customer : customers){


                boolean hasRecentOrder =
                        orderRepo.existsByTenantIdAndCompanyNameAndOrderDateAfter(
                                tenantId,
                                customer.getCompanyName(),
                                tenDaysAgo
                        );


                if(!hasRecentOrder){

                    inactiveCompanies.add(
                            customer.getCompanyName()
                    );

                }

            }



            if(!inactiveCompanies.isEmpty()){


                companyProfileModel company =
                        companyRepository
                                .findByTenantId(tenantId)
                                .orElse(null);


                if(company != null && company.getEmail() != null){


                    emailService.sendReminderMail(
                            inactiveCompanies,
                            company.getEmail()
                    );


                }

            }

        }

    }

}
