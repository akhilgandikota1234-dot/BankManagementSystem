package com.BankManagementSystem.app;

import com.BankManagementSystem.model.*;
import com.BankManagementSystem.service.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Main {
    private static final String DATA_FILE = "bankmanagementsystem.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        BankService bank = new BankService(accountService);
        FileService fileService = new FileService();

        try {
            accountService.replaceAll(fileService.loadAccounts(DATA_FILE));
            System.out.println("Loaded " + accountService.getAllAccounts().size() + " account(s).");
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose option: ");
            try {
                switch (choice) {
                    case 1 -> createAccount(bank);
                    case 2 -> deposit(bank);
                    case 3 -> withdraw(bank);
                    case 4 -> transfer(bank);
                    case 5 -> search(bank);
                    case 6 -> listAccounts(bank);
                    case 7 -> concurrentDeposits(bank);
                    case 8 -> save(bank, fileService);
                    case 9 -> {
                        save(bank, fileService);
                        running = false;
                        System.out.println("Thank you for using the Bank Management System.");
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\\n========== BANK ACCOUNT MANAGEMENT SYSTEM ==========");
        System.out.println("1. Create account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer funds");
        System.out.println("5. Search account");
        System.out.println("6. Display accounts (sorted by account number)");
        System.out.println("7. Run concurrent deposits (demo)");
        System.out.println("8. Save data");
        System.out.println("9. Save and exit");
    }

    private static void createAccount(BankService bank) {
        System.out.print("Customer ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Customer name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        Customer customer = new Customer(id, name, phone, email);

        System.out.print("Account number: ");
        String number = scanner.nextLine().trim();
        System.out.println("1. Savings  2. Current");
        int type = readInt("Account type: ");
        double balance = readDouble("Opening balance: ");

        if (type == 1) {
            double rate = readDouble("Annual interest rate (%): ");
            bank.createSavingsAccount(number, customer, balance, rate);
        } else if (type == 2) {
            double limit = readDouble("Overdraft limit: ");
            bank.createCurrentAccount(number, customer, balance, limit);
        } else {
            throw new IllegalArgumentException("Choose account type 1 or 2.");
        }
        System.out.println("Account created successfully.");
    }

    private static void deposit(BankService bank) {
        System.out.print("Account number: ");
        String number = scanner.nextLine().trim();
        bank.deposit(number, readDouble("Deposit amount: "));
        System.out.println("Deposit successful.");
    }

    private static void withdraw(BankService bank) {
        System.out.print("Account number: ");
        String number = scanner.nextLine().trim();
        bank.withdraw(number, readDouble("Withdrawal amount: "));
        System.out.println("Withdrawal successful.");
    }

    private static void transfer(BankService bank) {
        System.out.print("From account: ");
        String from = scanner.nextLine().trim();
        System.out.print("To account: ");
        String to = scanner.nextLine().trim();
        bank.transfer(from, to, readDouble("Transfer amount: "));
        System.out.println("Transfer successful.");
    }

    private static void search(BankService bank) {
        System.out.print("Account number: ");
        System.out.println(bank.findAccount(scanner.nextLine().trim()));
    }

    private static void listAccounts(BankService bank) {
        List<Account> accounts = bank.listAccounts();
        if (accounts.isEmpty()) System.out.println("No accounts found.");
        else accounts.forEach(System.out::println);
        System.out.println("\\nSorted by balance:");
        bank.sortByBalance().forEach(System.out::println);
    }

    private static void concurrentDeposits(BankService bank)
            throws InterruptedException, ExecutionException {
        System.out.print("Account number: ");
        String number = scanner.nextLine().trim();
        int count = readInt("Number of deposit tasks: ");
        List<Double> amounts = new ArrayList<>();
        for (int i = 0; i < count; i++) amounts.add(readDouble("Amount for task " + (i + 1) + ": "));
        try (ConcurrentTransactionService service = new ConcurrentTransactionService(3)) {
            for (String result : service.runDeposits(bank, number, amounts)) System.out.println(result);
        }
        System.out.println("New balance: " + bank.findAccount(number).getBalance());
    }

    private static void save(BankService bank, FileService fileService) throws IOException {
        fileService.saveAccounts(bank.getAccountService().getAccountMap(), DATA_FILE);
        System.out.println("Data saved to " + DATA_FILE);
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid amount.");
            }
        }
    }
}