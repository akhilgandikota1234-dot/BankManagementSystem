package com.BankManagementSystem.service;

import com.BankManagementSystem.model.Account;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        Objects.requireNonNull(account, "Account cannot be null.");
        if (account.getAccountNumber() == null || account.getAccountNumber().isBlank())
            throw new IllegalArgumentException("Account number is required.");
        if (accounts.putIfAbsent(account.getAccountNumber(), account) != null)
            throw new IllegalArgumentException("Account number already exists.");
    }

    public Account findAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) throw new IllegalArgumentException("Account not found.");
        return account;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> getSortedAccounts() {
        List<Account> result = getAllAccounts();
        Collections.sort(result); // Account implements Comparable
        return result;
    }

    public List<Account> getAccountsSortedByBalance() {
        List<Account> result = getAllAccounts();
        result.sort(Comparator.comparingDouble(Account::getBalance));
        return result;
    }

    public Map<String, Account> getAccountMap() {
        return new HashMap<>(accounts);
    }

    public void replaceAll(Map<String, Account> loadedAccounts) {
        accounts.clear();
        if (loadedAccounts != null) accounts.putAll(loadedAccounts);
    }
}