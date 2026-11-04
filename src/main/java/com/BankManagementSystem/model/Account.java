package com.BankManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "accountType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SavingsAccount.class, name = "savings"),
    @JsonSubTypes.Type(value = CurrentAccount.class, name = "current")
})
public abstract class Account implements Comparable<Account> {
    private String accountNumber;
    private Customer customer;
    private double balance;

    public Account() { }

    public Account(String accountNumber, Customer customer, double balance) {
        if (balance < 0) throw new IllegalArgumentException("Opening balance cannot be negative.");
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        balance += amount;
    }

    public synchronized void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
    }

    public abstract String getAccountType();

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public synchronized double getBalance() { return balance; }
    public synchronized void setBalance(double balance) {
        if (balance < 0) throw new IllegalArgumentException("Balance cannot be negative.");
        this.balance = balance;
    }

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareToIgnoreCase(other.accountNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account other)) return false;
        return Objects.equals(accountNumber, other.accountNumber);
    }

    @Override
    public int hashCode() { return Objects.hash(accountNumber); }

    @Override
    public String toString() {
        return getAccountType() + " Account{number='" + accountNumber +
                "', customer=" + customer + ", balance=" + String.format("%.2f", getBalance()) + "}";
    }
}