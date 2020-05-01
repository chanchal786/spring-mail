package com.poc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.poc.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
