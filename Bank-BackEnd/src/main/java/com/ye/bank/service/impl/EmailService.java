package com.ye.bank.service.impl;

import com.ye.bank.dto.EmailDetails;
import com.ye.bank.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage , true);
            mimeMessageHelper.setFrom(sendEmail);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            FileSystemResource file = new FileSystemResource(emailDetails.getAttachment());
            mimeMessageHelper.addAttachment(file.getFilename() , file );

            mailSender.send(mimeMessage);

            log.info(file.getFilename() + " has been sent to user with email ");

        }catch ( MessagingException e ) {
            throw new RuntimeException(e);
        }
    }
}
