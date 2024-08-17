package com.example.polls.bank.interfaces;

import com.example.polls.bank.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer saveCustomer(Customer c);

    Optional<Customer> findCustomerById(Integer id);

    List<Customer> createDummyCustomers(Integer numberOfCustomers);

    List<Customer> findAllCustomers();
}
