package com.poc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import com.poc.email.service.EmailService;
import com.poc.model.Customer;
import com.poc.repository.CustomerRepository;
import com.poc.utils.ExcelGenerator;

import javax.activation.DataSource;
import javax.mail.MessagingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private ExcelGenerator excelGenerator;

	@Autowired
	private CustomerRepository customerRepository;

	private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);
	@Autowired
	EmailService emailService;

	@GetMapping(value = "/simple-email/{email}")
	public @ResponseBody ResponseEntity<String> sendSimpleEmail(@PathVariable("email") String email) {

		try {
			emailService.sendSimpleEmail(email, "Welcome", "This is a welcome email for your!!");
		} catch (MailException mailException) {
			LOG.error("Error while sending out email..{}", mailException.fillInStackTrace());
			return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Please check your inbox", HttpStatus.OK);
	}

	@GetMapping(value = "/simple-order-email/{user-email}")
	public @ResponseBody ResponseEntity<String> sendEmailAttachment(@PathVariable("user-email") String email)
			throws IOException {

		try {

			emailService.sendEmailWithAttachment(email, "Order Confirmation", "Thanks for using our spring-email service api",
					"classpath:purchase_order.pdf");

		} catch (MessagingException | FileNotFoundException mailException) {
			LOG.error("Error while sending out email..{}", mailException.fillInStackTrace());
			return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Please check your inbox for order confirmation", HttpStatus.OK);
	}

	@GetMapping(value = "/send-report/{user-email}")
	public ResponseEntity<?> sendEmailExcel(@PathVariable("user-email") String email)
			throws IOException {
		String fileName = "";

		try {
			List<Customer> fetchedCustomers = (List<Customer>) customerRepository.findAll();
			DataSource attachment = excelGenerator.customersToExcel2(fetchedCustomers);
			fileName = emailService.sendEmailWithExcel(email, "Report", "Thanks for using our spring-email service api",
					attachment);
		} catch (MessagingException | FileNotFoundException mailException) {
			LOG.error("Error while sending out email..{}", mailException.fillInStackTrace());
			return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("Report have been shared to "+email+" with filename " + fileName, HttpStatus.OK);
	}

}
