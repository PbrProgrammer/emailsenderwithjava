package com.clarity.emailsms.service;


import com.clarity.emailsms.kafka.dto.EmailKafkaDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Component;


@Component
public class EmailSender {

private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendSimpleMessage(EmailKafkaDTO dto) {

        String to= dto.getEmailTo();
        String subject= dto.getEmailSubject();
        String text= dto.getEmailBody();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }



}
