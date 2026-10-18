package com.skillswap.backend.dto;

public class SessionStatusDto {

    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public SessionStatusDto() {
    }

    public SessionStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
