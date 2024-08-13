package com.example.polls.bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    @Column(unique = true)
    private String accountNumber;
    private BigDecimal balance = new BigDecimal(0);
    private BigDecimal previousMonthTurnover = new BigDecimal(0);
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
////    @JsonIgnoreProperties("accounts")
     @JsonBackReference
//    @JsonManagedReference*/
    private Customer customer;

    public Account(String accountNumber) {
        this.setAccountNumber(accountNumber);
    }

    public Account() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    public synchronized void updateBalance(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.balance = this.balance.add(amount);
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPreviousMonthTurnover() {
        return previousMonthTurnover;
    }

    public void setPreviousMonthTurnover(BigDecimal previousMonthTurnover) {
        this.previousMonthTurnover = previousMonthTurnover;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
