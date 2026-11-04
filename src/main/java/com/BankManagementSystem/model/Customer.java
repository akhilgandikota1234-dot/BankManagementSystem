package com.BankManagementSystem.model;

import java.util.Objects;

public class Customer implements Comparable<Customer> {
    private String customerId;
    private String name;
    private String phone;
    private String email;

    public Customer() { }

    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public int compareTo(Customer other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer other)) return false;
        return Objects.equals(customerId, other.customerId);
    }

    @Override
    public int hashCode() { return Objects.hash(customerId); }

    @Override
    public String toString() {
        return "Customer{ID='" + customerId + "', name='" + name +
                "', phone='" + phone + "', email='" + email + "'}";
    }
}