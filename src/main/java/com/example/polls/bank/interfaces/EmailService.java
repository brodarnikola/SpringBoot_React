package com.example.polls.bank.interfaces;


import com.example.polls.bank.model.Customer;
import com.example.polls.bank.model.Transaction;

import java.math.BigDecimal;

public interface EmailService {
    void sendEmail(String sendTo, String subject, String body);

    void sendEmailImpl(Transaction transaction, String accountNumber, Customer customer, boolean sent, BigDecimal currentBalance);
}
