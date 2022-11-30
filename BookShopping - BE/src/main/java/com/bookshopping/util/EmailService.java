package com.bookshopping.util;

public interface EmailService {
    boolean sendEmail(String to, String subject, String message);
}
