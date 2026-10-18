package com.skillswap.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a scheduled skill exchange session between two students.
 */
@Entity
@Table(name = "skill_sessions")
public class SkillSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId; // ID of the logged-in student (e.g. Gargi)

    @Column(nullable = false)
    private Long partnerId; // ID of the skill partner

    private String partnerName;
    private String partnerInitials;

    @Column(nullable = false)
    private String topic;

    private String sessionType; // Teaching, Learning, Exchange
    private String sessionDate; // YYYY-MM-DD
    private String sessionDay;  // e.g. Monday
    private String startTime;   // e.g. 18:00
    private String endTime;     // e.g. 19:00
    private Integer durationMinutes;

    @Column(name = "session_mode")
    private String mode;        // Online, Offline

    private Integer coins;      // SkillCoins reward upon completion
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED

    private LocalDateTime createdAt = LocalDateTime.now();

    public SkillSession() {
    }

    public SkillSession(Long studentId, Long partnerId, String partnerName, String partnerInitials, 
                        String topic, String sessionType, String sessionDate, String sessionDay, 
                        String startTime, String endTime, Integer durationMinutes, String mode, 
                        Integer coins, String status) {
        this.studentId = studentId;
        this.partnerId = partnerId;
        this.partnerName = partnerName;
        this.partnerInitials = partnerInitials;
        this.topic = topic;
        this.sessionType = sessionType;
        this.sessionDate = sessionDate;
        this.sessionDay = sessionDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.mode = mode;
        this.coins = coins;
        this.status = status != null ? status : "SCHEDULED";
        this.createdAt = LocalDateTime.now();
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

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerInitials() {
        return partnerInitials;
    }

    public void setPartnerInitials(String partnerInitials) {
        this.partnerInitials = partnerInitials;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionDay() {
        return sessionDay;
    }

    public void setSessionDay(String sessionDay) {
        this.sessionDay = sessionDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
