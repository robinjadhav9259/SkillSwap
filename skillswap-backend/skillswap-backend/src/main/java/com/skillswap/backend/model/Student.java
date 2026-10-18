package com.skillswap.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entity representing a Student on the SkillSwap platform.
 * Stores personal profile details, skills offered (teach), skills sought (learn),
 * and exchange statistics (coins, hours, sessions, streak).
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String course;

    @Column(name = "academic_year")
    private String year;

    private String level; // Beginner, Intermediate, Advanced

    @Column(length = 1000)
    private String bio;

    // Comma-separated lists of skills for easy storage and human-readable SQL inspection
    @Column(length = 1000)
    private String teachSkills;

    @Column(length = 1000)
    private String learnSkills;

    private Integer coins = 760;
    private Double hours = 18.4;
    private Integer sessions = 18;
    private Integer streak = 12;

    private Boolean online = true;
    private String category = "coding"; // coding, design, business
    private Integer experience = 85;

    public Student() {
    }

    public Student(String name, String email, String course, String year, String level, 
                   String bio, String teachSkills, String learnSkills, Integer coins, 
                   Double hours, Integer sessions, Integer streak, Boolean online, 
                   String category, Integer experience) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.year = year;
        this.level = level;
        this.bio = bio;
        this.teachSkills = teachSkills;
        this.learnSkills = learnSkills;
        this.coins = coins;
        this.hours = hours;
        this.sessions = sessions;
        this.streak = streak;
        this.online = online;
        this.category = category;
        this.experience = experience;
    }

    // Helper to get initials (e.g. "Gargi Dhabale" -> "GD")
    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return "SS";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    // Helper methods for list of skills
    public List<String> getTeachList() {
        if (teachSkills == null || teachSkills.trim().isEmpty()) return new ArrayList<>();
        return Arrays.stream(teachSkills.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .toList();
    }

    public List<String> getLearnList() {
        if (learnSkills == null || learnSkills.trim().isEmpty()) return new ArrayList<>();
        return Arrays.stream(learnSkills.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .toList();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTeachSkills() {
        return teachSkills;
    }

    public void setTeachSkills(String teachSkills) {
        this.teachSkills = teachSkills;
    }

    public String getLearnSkills() {
        return learnSkills;
    }

    public void setLearnSkills(String learnSkills) {
        this.learnSkills = learnSkills;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Integer getSessions() {
        return sessions;
    }

    public void setSessions(Integer sessions) {
        this.sessions = sessions;
    }

    public Integer getStreak() {
        return streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }
}
