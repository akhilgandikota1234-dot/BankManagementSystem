package com.BankManagementSystem.model;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount() { }

    public SavingsAccount(String accountNumber, Customer customer, double balance, double interestRate) {
        super(accountNumber, customer, balance);
        if (interestRate < 0) throw new IllegalArgumentException("Interest rate cannot be negative.");
        this.interestRate = interestRate;
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    @Override
    public String getAccountType() { return "Savings"; }

    public double calculateInterest() {
        return getBalance() * interestRate / 100.0;
    }

    @Override
    public String toString() {
        return super.toString() + ", interestRate=" + interestRate + "%";
    }
}