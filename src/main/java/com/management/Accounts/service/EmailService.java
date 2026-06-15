package com.management.Accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
    public class EmailService {
        @Autowired
        private JavaMailSender mailSender;
        public void sendReminderMail(List<String> companies) {

            StringBuilder body = new StringBuilder();

            body.append("The following companies have not placed any orders in the last 10 days:\n\n");

            companies.forEach(company ->
                    body.append("- ").append(company).append("\n"));

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo("vigneshuvari@gmail.com"); // உங்க mail id
            message.setSubject("Order Reminder");
            message.setText(body.toString());

            mailSender.send(message);
        }
    }
