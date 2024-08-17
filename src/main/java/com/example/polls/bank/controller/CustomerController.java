package com.example.polls.bank.controller;

import com.example.polls.bank.interfaces.CustomerService;
import com.example.polls.bank.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
