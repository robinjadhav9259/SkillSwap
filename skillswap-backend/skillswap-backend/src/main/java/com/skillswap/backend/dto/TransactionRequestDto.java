package com.skillswap.backend.dto;

public class TransactionRequestDto {

    private String title;
    private Integer amount;
    private String type; // CREDIT or DEBIT

    public TransactionRequestDto() {
    }

    public TransactionRequestDto(String title, Integer amount, String type) {
        this.title = title;
        this.amount = amount;
        this.type = type;
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
}
