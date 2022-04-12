package programmers.spring.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private final UUID customer_id;
    private String name;
    private final String email;
    private LocalDateTime lastLoginAt;
    private final LocalDateTime createdAt;

    public Customer(UUID customer_id, String name, String email, LocalDateTime createdAt) {
        this.name = name;
        this.customer_id = customer_id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Customer(UUID customer_id, String name, String email, LocalDateTime lastLoginAt, LocalDateTime createdAt) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    public void validateName(String name) {
        if (name.isBlank()) {
            throw new RuntimeException("Name Should not be blank");
        }
    }

    public UUID getCustomer_id() {
        return customer_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
