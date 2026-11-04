package com.BankManagementSystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentTransactionService implements AutoCloseable {
    private final ExecutorService executor;

    public ConcurrentTransactionService(int poolSize) {
        if (poolSize < 1) throw new IllegalArgumentException("Pool size must be at least 1.");
        executor = Executors.newFixedThreadPool(poolSize);
    }

    public List<String> runDeposits(BankService bank, String accountNumber, List<Double> amounts)
            throws InterruptedException, ExecutionException {
        List<Callable<String>> tasks = new ArrayList<>();
        for (double amount : amounts) {
            tasks.add(() -> {
                bank.deposit(accountNumber, amount);
                return Thread.currentThread().getName() + " deposited " + amount;
            });
        }
        List<Future<String>> futures = executor.invokeAll(tasks);
        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) results.add(future.get());
        return results;
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}