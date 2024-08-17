package com.example.polls.bank.interfaces;

import com.example.polls.bank.model.Customer;
import com.example.polls.bank.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    Optional<List<Transaction>> fetchSenderTransactions(Customer customer);

    Optional<List<Transaction>> fetchReceiverTransactions(Customer customer);

    Optional<List<Transaction>> getTransactionHistory(Customer customer);

    Optional<List<Transaction>> getTransactionHistoryFiltered(Customer customer, String t_side, String t_side_value);

    List<Transaction> fetchReceiverTransactionsForAccount(String account);

    List<Transaction> fetchSenderTransactionsForAccount(String account);

    Integer processTransaction(Transaction transaction);

}
