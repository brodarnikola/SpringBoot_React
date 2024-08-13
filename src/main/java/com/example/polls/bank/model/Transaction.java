package com.example.polls.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {
    @NotNull
    private String senderAccount;
    @NotNull
    private String receiverAccount;
    @NotNull
    private BigDecimal amount;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @NotNull
    private String message;
    @NotNull
    private Date timestamp;

    public Transaction() {

    }

    public Transaction(String senderAccountId, String receiverAccountId, BigDecimal amount, Currency currency, String message, Date timestamp) {
        this.senderAccount = senderAccountId;
        this.receiverAccount = receiverAccountId;
        this.amount = amount;
        this.currency = currency;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;


    public @NotNull String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(@NotNull String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public @NotNull String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(@NotNull String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public @NotNull BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotNull BigDecimal amount) {
        this.amount = amount;
    }

    public @NotNull Currency getCurrency() {
        return currency;
    }

    public void setCurrency(@NotNull Currency currency) {
        this.currency = currency;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    public @NotNull Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }


}
