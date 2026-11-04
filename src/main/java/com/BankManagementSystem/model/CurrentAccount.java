package com.BankManagementSystem.model;

public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount() { }

    public CurrentAccount(String accountNumber, Customer customer, double balance, double overdraftLimit) {
        super(accountNumber, customer, balance);
        if (overdraftLimit < 0) throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    @Override
    public synchronized void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (amount > getBalance() + overdraftLimit)
            throw new IllegalArgumentException("Amount exceeds balance and overdraft limit.");
        setBalance(getBalance() - amount);
    }

    @Override
    public String getAccountType() { return "Current"; }

    @Override
    public String toString() {
        return super.toString() + ", overdraftLimit=" + overdraftLimit;
    }
}