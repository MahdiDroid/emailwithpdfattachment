package com.example.secondmaildemo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FeedbackController {

    @Autowired
    private MailConfig mailConfig;

    @PostMapping({" ", "", "/"})
    public ResponseEntity<?> sendFeedback(@RequestBody Feedback feedback,
                                          BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }

        try {

            //mail sender
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(this.mailConfig.getHost());
            mailSender.setPort(this.mailConfig.getPort());
            mailSender.setUsername(this.mailConfig.getUsername());
            mailSender.setPassword(this.mailConfig.getPassword());

            // create
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("root@biyoteknik.com");
            mailMessage.setTo("cooperation@biyoteknik.com");
            mailMessage.setSubject("new feed back from " + feedback.getName());
            mailMessage.setText(feedback.getFeedback());
            //
            mailSender.send(mailMessage);

            return new ResponseEntity<>("email send successfully  ",HttpStatus.CREATED);

        } catch (Exception ex) {
            return new ResponseEntity("something came up failed ",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/sendmail")
    public ResponseEntity<?> sendmailWithAttachment(@Valid @RequestBody Feedback feedback,
                                                    BindingResult result){

        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }

        //mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.mailConfig.getHost());
        mailSender.setPort(this.mailConfig.getPort());
        mailSender.setUsername(this.mailConfig.getUsername());
        mailSender.setPassword(this.mailConfig.getPassword());

        //********************************************************
        MimeMessagePreparator preparator = new MimeMessagePreparator()
        {
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("cooperation@biyoteknik.com"));
                mimeMessage.setFrom(new InternetAddress("root@biyoteknik.com"));
                mimeMessage.setSubject("New cooperation : " + feedback.getName());
                mimeMessage.setText(feedback.getFeedback());
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.addAttachment(String.format("%s.pdf", feedback.getName()) ,
                        new ByteArrayResource(
                        new EmailUtility().Base64ToBytes(feedback.getFeedback() )),
                        "application/pdf"
                );
                helper.setText("",true);
            }
        };

        try {
            mailSender.send(preparator);
            return new ResponseEntity<>("email send successfully  ", HttpStatus.CREATED);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
            return new ResponseEntity("something came up failed ",HttpStatus.BAD_REQUEST);
        }
    }

}

