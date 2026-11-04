package com.BankManagementSystem.service;

import com.BankManagementSystem.model.*;
import java.util.List;

public class BankService {
    private final AccountService accountService;

    public BankService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createSavingsAccount(String number, Customer customer, double balance, double rate) {
        accountService.addAccount(new SavingsAccount(number, customer, balance, rate));
    }

    public void createCurrentAccount(String number, Customer customer, double balance, double overdraft) {
        accountService.addAccount(new CurrentAccount(number, customer, balance, overdraft));
    }

    public void deposit(String number, double amount) {
        accountService.findAccount(number).deposit(amount);
    }

    public void withdraw(String number, double amount) {
        accountService.findAccount(number).withdraw(amount);
    }

    public synchronized void transfer(String from, String to, double amount) {
        if (from.equals(to)) throw new IllegalArgumentException("Source and destination must differ.");
        Account source = accountService.findAccount(from);
        Account destination = accountService.findAccount(to);
        // Lock accounts in a stable order to avoid deadlocks during concurrent transfers.
        Account first = from.compareTo(to) < 0 ? source : destination;
        Account second = first == source ? destination : source;
        synchronized (first) {
            synchronized (second) {
                source.withdraw(amount);
                destination.deposit(amount);
            }
        }
    }

    public Account findAccount(String number) { return accountService.findAccount(number); }
    public List<Account> listAccounts() { return accountService.getSortedAccounts(); }
    public List<Account> sortByBalance() { return accountService.getAccountsSortedByBalance(); }
    public AccountService getAccountService() { return accountService; }
}