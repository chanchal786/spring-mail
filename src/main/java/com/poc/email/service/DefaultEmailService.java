package com.poc.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;

@Service
public class DefaultEmailService implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void sendSimpleEmail(String toAddress, String subject, String message) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(toAddress);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(message);
		emailSender.send(simpleMailMessage);
	}

	@Override
	public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
			throws MessagingException, FileNotFoundException {

		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		messageHelper.setText(message);
		FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
		messageHelper.addAttachment("Purchase_Order.pdf", file);
		emailSender.send(mimeMessage);
	}

	@Override
	public String sendEmailWithExcel(String toAddress, String subject, String message, DataSource attachment)
			throws MessagingException, FileNotFoundException {
		MimeMessage mimeMessage = emailSender.createMimeMessage();

		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
		messageHelper.setTo(toAddress);
		String fileName = "Report_" + System.currentTimeMillis() + ".xlsx";
		messageHelper.setSubject(subject + " || " + fileName);
		messageHelper.setText(message);
		messageHelper.addAttachment(fileName, attachment);
		emailSender.send(mimeMessage);
		return fileName;
	}
}
