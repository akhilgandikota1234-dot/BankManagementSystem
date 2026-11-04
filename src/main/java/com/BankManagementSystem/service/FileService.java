package com.BankManagementSystem.service;

import com.BankManagementSystem.model.Account;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class FileService {
    private final ObjectMapper mapper;

    public FileService() {
        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveAccounts(Map<String, Account> accounts, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        mapper.writeValue(path.toFile(), accounts);
    }

    public Map<String, Account> loadAccounts(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) return Map.of();
        return mapper.readValue(path.toFile(), new TypeReference<Map<String, Account>>() {});
    }
}