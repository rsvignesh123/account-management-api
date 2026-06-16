package com.management.Accounts.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    @Value("${MAIL_FROM}")
    private String fromEmail;

    public void sendReminderMail(List<String> companies) {

        StringBuilder body = new StringBuilder();
        body.append("The following companies have not placed orders:\n\n");

        for (String company : companies) {
            body.append("- ").append(company).append("\n");
        }

        Email from = new Email(fromEmail);
        Email to = new Email("rajanveerapalam@gmail.com"); // or dynamic

        Content content = new Content("text/plain", body.toString());
        Mail mail = new Mail(from, "Order Reminder", to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);

            System.out.println("Email sent successfully ✔");

        } catch (IOException e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }
}

