package com.etiya.CertificateTracker.core.utilities.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("halitmancar@gmail.com");
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(text);
        mailSender.send(smm);
    }
}
