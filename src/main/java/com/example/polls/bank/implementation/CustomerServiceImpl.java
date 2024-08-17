package com.example.polls.bank.implementation;

import com.example.polls.bank.dao.CustomerRepository;
import com.example.polls.bank.interfaces.CustomerService;
import com.example.polls.bank.model.Customer;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer c) {
        return customerRepository.save(c);
    }

    @Override
    public Optional<Customer> findCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> createDummyCustomers(Integer numberOfCustomers) {

        try {
            Faker faker = new Faker();
            List<Customer> customers = new ArrayList<>();

            String email1 = "brodarnikola9@gmail.com";
            String email2 = "brodarnikola@gmail.com";

            for (int i = 0; i < numberOfCustomers; i++) {
                Customer customer = new Customer();
                customer.setName(faker.name().fullName());
                customer.setAddress(faker.address().fullAddress());
                if(i % 2 == 0) {
                    customer.setEmail(email1);
                }
                else {
                    customer.setEmail(email2);
                }
                customers.add(customer);
            }

            customerRepository.saveAll(customers);
            return customers;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
