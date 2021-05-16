package com.artur.service;

import com.artur.entity.Account;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class SendNewPasswordListener implements ApplicationListener<com.artur.service.SendNewPasswordEven> {


    private final JavaMailSender mailSender;

    public SendNewPasswordListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(com.artur.service.SendNewPasswordEven event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(com.artur.service.SendNewPasswordEven event) {
        Account account = event.getAccount();

        String recipientAddress = account.getEmail();
        String subject = "New password for Plushkin";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Your new password: " + event.getNewPassword());
        mailSender.send(email);
    }
}
