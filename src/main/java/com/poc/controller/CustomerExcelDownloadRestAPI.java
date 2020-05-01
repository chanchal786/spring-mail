package com.poc.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.model.Customer;
import com.poc.utils.ExcelGenerator;

@RestController
@RequestMapping("/api/customers")
public class CustomerExcelDownloadRestAPI {
	
	 List<Customer> customers = Arrays.asList(
	          new Customer(Long.valueOf(1), "Jack Smith", "Massachusetts", 23),
	          new Customer(Long.valueOf(2), "Adam Johnson", "New York", 27),
	          new Customer(Long.valueOf(3), "Katherin Carter", "Washington DC", 26),
	          new Customer(Long.valueOf(4), "Jack London", "Nevada", 33), 
	          new Customer(Long.valueOf(5), "Jason Bourne", "California", 36));
	
	@GetMapping(value = "/download/customers.xlsx")
    public ResponseEntity<InputStreamResource> excelCustomersReport() throws IOException {
     
    ByteArrayInputStream in = ExcelGenerator.customersToExcel(customers);
    // return IOUtils.toByteArray(in);
    
    HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");
    
     return ResponseEntity
                  .ok()
                  .headers(headers)
                  .body(new InputStreamResource(in));
    }
}
