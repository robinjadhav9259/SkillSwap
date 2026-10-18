package com.skillswap.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a SkillCoins ledger transaction (credit/debit).
 */
@Entity
@Table(name = "coin_transactions")
public class CoinTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer amount; // Always positive magnitude

    @Column(name = "transaction_type", nullable = false)
    private String type; // CREDIT or DEBIT

    private String dateDescription; // e.g. "Today", "Yesterday" or formatted timestamp

    @Column(name = "created_at")
    private LocalDateTime timestamp = LocalDateTime.now();

    public CoinTransaction() {
    }

    public CoinTransaction(Long studentId, String title, Integer amount, String type, String dateDescription) {
        this.studentId = studentId;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.dateDescription = dateDescription;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateDescription() {
        return dateDescription;
    }

    public void setDateDescription(String dateDescription) {
        this.dateDescription = dateDescription;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
