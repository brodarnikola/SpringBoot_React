package com.example.polls.bank.dao;

import com.example.polls.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.senderAccount = :account")
    List<Transaction> findBySenderAccount(@Param("account") String account);

    @Query("SELECT t FROM Transaction t WHERE t.receiverAccount = :account")
    List<Transaction> findByReceiverAccount(@Param("account") String account);

    @Query("SELECT t FROM Transaction t WHERE t.receiverAccount = :account OR t.senderAccount = :account")
    List<Transaction> getTransactionHistory(String account);
}
