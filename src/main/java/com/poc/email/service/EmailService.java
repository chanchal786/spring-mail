package com.poc.email.service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import java.io.FileNotFoundException;

public interface EmailService {

    void sendSimpleEmail(String toAddress, String subject, String message);
    void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment) throws MessagingException, FileNotFoundException;
    String sendEmailWithExcel(String toAddress, String subject, String message, DataSource attachment) throws MessagingException, FileNotFoundException;
}
