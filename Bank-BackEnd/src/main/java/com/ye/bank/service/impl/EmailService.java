package com.ye.bank.service.impl;

import com.ye.bank.dto.EmailDetails;
import com.ye.bank.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendEmail;

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(sendEmail);
            message.setTo(emailDetails.getRecipient());
            message.setText(emailDetails.getMessageBody());
            message.setSubject(emailDetails.getSubject());

            mailSender.send(message);
            System.out.println("Mail sent successfully");
        }catch( Exception e ){
            throw new RuntimeException(e);
        }
    }
}
