package com.bookshopping.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

//    @Value("${spring.mail.username}") private String sender;
    @Override
    public boolean sendEmail(String to, String subject, String message) {
        try {
            System.out.println("Send email");
            MimeMessage msg = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
            helper.setFrom("luongquanghuynh264@gamil.com","Bookstore");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            javaMailSender.send(msg);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Send email error");
            return false;
        }
    }
}
