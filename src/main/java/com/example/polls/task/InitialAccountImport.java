package com.example.polls.task;


import com.example.polls.bank.implementation.CustomerServiceImpl;
import com.example.polls.bank.interfaces.AccountService;
import com.example.polls.bank.interfaces.TransactionService;
import com.example.polls.bank.model.Account;
import com.example.polls.bank.model.Customer;
import com.example.polls.bank.model.Transaction;
import com.example.polls.bank.model.Currency;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class InitialAccountImport implements Runnable {

    private static final int THREAD_POOL_SIZE = 10;
    private static final int NUMBER_OF_CUSTOMERS = 2;
    private static final String FILE_PATH = "src/main/resources/small_transactions_file";

    private static final Logger log = LoggerFactory.getLogger(InitialAccountImport.class);

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    CustomerServiceImpl customerService;

    @Override
    public void run() {
        initialDataImport();
    }

    public void initialDataImport() {

        List<Customer> initialCustomers = customerService.findAllCustomers();
        if(initialCustomers.size() < NUMBER_OF_CUSTOMERS) {

            List<Transaction> transactions = importAllTransactions(FILE_PATH);
            List<Customer> customers = customerService.createDummyCustomers(NUMBER_OF_CUSTOMERS);
            Set<Account> accounts = getUniqueAccounts(transactions);

            setCustomerAccounts(customers,accounts);

            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            CountDownLatch latch = new CountDownLatch(transactions.size());

            log.info("Size of accounts is: " + accounts.size());
            log.info("Account list is: " + accounts);
            log.info("Size of transaction is: " + transactions.size());

            transactions.forEach(t -> executor.submit(() -> {
                try {
                    processTransaction(t, accounts);
                } finally {
                    latch.countDown();
                }
            }));

            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            executor.shutdown();

            accountService.saveAll(accounts);


            log.info("Size of transaction is: " + transactions.size());

            log.info("Finish with import");
        }

    }


    private void processTransaction(Transaction t, Set<Account> accounts) {

        Optional<Account> sender = accounts.stream()
                .filter(a -> a.getAccountNumber().equals(t.getSenderAccount()))
                .findFirst();

        Optional<Account> receiver = accounts.stream()
                .filter(a -> a.getAccountNumber().equals(t.getReceiverAccount()))
                .findFirst();

        if (sender.isPresent() && receiver.isPresent()) {

            sender.get().updateBalance(t.getAmount().negate());
            receiver.get().updateBalance(t.getAmount());

            transactionService.saveTransaction(t);
        }
    }


    private Set<Account> getUniqueAccounts(List<Transaction> transactions) {
        // this is code, to get unique accounts when using lombok library.. LomBook automatically builds for you getter, setter, constructors
//        return transactions.stream()
//                .flatMap(t -> Stream.of(
//                        new Account(t.getSenderAccount()),
//                        new Account(t.getReceiverAccount())
//                ))
//                .collect(Collectors.toSet());
        Map<String, Account> accountMap = new HashMap<>();

        for (Transaction t : transactions) {
            accountMap.putIfAbsent(t.getSenderAccount(), new Account(t.getSenderAccount()));
            accountMap.putIfAbsent(t.getReceiverAccount(), new Account(t.getReceiverAccount()));
        }

        return new HashSet<>(accountMap.values());
    }

    public List<Transaction> importAllTransactions(String csvFile) {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                Transaction transaction = new Transaction(
                        data[1].trim(), // senderAccountId
                        data[2].trim(), // receiverAccountId
                        new BigDecimal(data[3].trim()), // amount
                        Currency.valueOf(data[4].trim()), // currency
                        data[5].trim(), // message
                        Date.valueOf(data[6].trim()) // timestamp
                );
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }
    private void setCustomerAccounts(List<Customer> customers, Set<Account> accounts) {
        accounts.forEach(a -> a.setCustomer(RandomUtils.getRandomItem(customers)));
    }
}
