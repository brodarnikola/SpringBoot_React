package com.example.polls.bank.implementation;

import com.example.polls.bank.dao.TransactionRepository;
import com.example.polls.bank.interfaces.AccountService;
import com.example.polls.bank.interfaces.EmailService;
import com.example.polls.bank.interfaces.TransactionService;
import com.example.polls.bank.model.Account;
import com.example.polls.bank.model.Customer;
import com.example.polls.bank.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    public static final String FILTER_NAME = "SIDE";
    public static final String FILTER_SENDER = "SENDER";
    public static final String FILTER_RECEIVER = "RECEIVER";

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    public Integer processTransaction(Transaction transaction) {
        Transaction trans = transactionRepository.save(transaction);

        Account sender = accountService.findByAccountNumber(transaction.getSenderAccount()).orElseGet(Account::new);
        Account receiver = accountService.findByAccountNumber(transaction.getReceiverAccount()).orElseGet(Account::new);

        updateAccountAmount(transaction, sender, receiver);
        sendEmailsToReceiverAndSender(transaction, sender, receiver);
        return trans.getTransactionId();
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Optional<List<Transaction>> getTransactionHistory(Customer customer) {
        List<Transaction> transactions = new ArrayList<>();
        customer.getAccounts().forEach(account -> transactions.addAll(transactionRepository.getTransactionHistory(account.getAccountNumber())));

        System.out.println(transactions.size());
        return Optional.of(transactions);
    }

    @Override
    public Optional<List<Transaction>> getTransactionHistoryFiltered(Customer customer, String filterName, String filterValue) {
        if (filterName.equalsIgnoreCase(FILTER_NAME)) {
            switch (filterValue.toUpperCase()) {
                case FILTER_SENDER:
                    // fetch, get sender transactions
                    return fetchSenderTransactions(customer);
                case FILTER_RECEIVER:
                    return fetchReceiverTransactions(customer);
                default:
            }
        }
        return Optional.of(new ArrayList<>());
    }

    @Override
    public Optional<List<Transaction>> fetchSenderTransactions(Customer customer) {
        List<Transaction> transactions = new ArrayList<>();
        customer.getAccounts().forEach(account -> transactions.addAll(transactionRepository.findBySenderAccount(account.getAccountNumber())));

        return Optional.of(transactions);
    }

    @Override
    public Optional<List<Transaction>> fetchReceiverTransactions(Customer customer) {
        List<Transaction> transactions = new ArrayList<>();
        customer.getAccounts().forEach(account -> transactions.addAll(transactionRepository.findByReceiverAccount(account.getAccountNumber())));

        return Optional.of(transactions);
    }

    @Override
    public List<Transaction> fetchReceiverTransactionsForAccount(String account) {
        return transactionRepository.findByReceiverAccount(account);
    }

    @Override
    public List<Transaction> fetchSenderTransactionsForAccount(String account) {
        return transactionRepository.findBySenderAccount(account);
    }

    private void sendEmailsToReceiverAndSender(Transaction transaction, Account sender, Account receiver) {
        setCustomerForAccount(sender);
        setCustomerForAccount(receiver);
        emailService.sendEmailImpl(transaction, sender.getAccountNumber(), sender.getCustomer(), true, sender.getBalance());
        emailService.sendEmailImpl(transaction, receiver.getAccountNumber(), receiver.getCustomer(), false, receiver.getBalance());
    }

    private void updateAccountAmount(Transaction transaction, Account sender, Account receiver) {

        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));
        accountService.save(sender);
        accountService.save(receiver);
    }

    private void setCustomerForAccount(Account account) {
        Customer senderCustomer = new Customer();
        senderCustomer.setCustomerId(account.getCustomer().getCustomerId());
        senderCustomer.setName(account.getCustomer().getName());
        senderCustomer.setAddress(account.getCustomer().getAddress());
        senderCustomer.setEmail(account.getCustomer().getEmail());
        account.setCustomer(senderCustomer);
    }


}
