package com.example.polls.bank.controller;


import com.example.polls.bank.implementation.CustomerServiceImpl;
import com.example.polls.bank.implementation.TransactionServiceImpl;
import com.example.polls.bank.model.Customer;
import com.example.polls.bank.model.Transaction;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    public static final String FILTER_NAME = "SIDE";
    @Autowired
    TransactionServiceImpl transactionService;
    @Autowired
    CustomerServiceImpl customerService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/transaction/history/{customerId}")
    public ResponseEntity<?> getTransactionHistory(@Valid @PathVariable Integer customerId,
                                                                   @RequestParam(required = false, name = FILTER_NAME) String filterValue) {

        logger.info("Received customerId: {}", customerId);
        logger.info("Received filterValue: {}", filterValue);

        Optional<List<Transaction>> transactionHistory = Optional.empty();
        Optional<Customer> customerById = customerService.findCustomerById(customerId);

        if(customerById.isPresent()) {

            if (filterValue != null) {
                transactionHistory = transactionService.getTransactionHistoryFiltered(customerById.get(), FILTER_NAME, filterValue);
            } else {
                transactionHistory = transactionService.getTransactionHistory(customerById.get());
            }

            return ResponseEntity.ok(transactionHistory);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> processTransaction(@RequestBody Transaction transaction) {

        Integer result = transactionService.processTransaction(transaction);
        if (result != null) {
            return ResponseEntity.ok(result.toString());
        } else {
            return ResponseEntity.status(500).body("Transaction processing failed");
        }
    }
}
