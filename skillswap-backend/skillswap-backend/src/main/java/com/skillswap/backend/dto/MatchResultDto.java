package com.skillswap.backend.dto;

import java.util.List;

/**
 * Data Transfer Object representing a skill match between the current student and a partner.
 * Contains explainable matching metrics for college presentations.
 */
public class MatchResultDto {

    private Long id;
    private String name;
    private String initials;
    private List<String> teach;
    private List<String> learn;
    private String category;
    private Integer score; // Overall match score %
    private Boolean online;
    private String level;
    private Integer experience;
    private String explanation; // Plain-English explanation of why this match was made

    public MatchResultDto() {
    }

    public MatchResultDto(Long id, String name, String initials, List<String> teach, List<String> learn, 
                          String category, Integer score, Boolean online, String level, 
                          Integer experience, String explanation) {
        this.id = id;
        this.name = name;
        this.initials = initials;
        this.teach = teach;
        this.learn = learn;
        this.category = category;
        this.score = score;
        this.online = online;
        this.level = level;
        this.experience = experience;
        this.explanation = explanation;
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

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public List<String> getTeach() {
        return teach;
    }

    public void setTeach(List<String> teach) {
        this.teach = teach;
    }

    public List<String> getLearn() {
        return learn;
    }

    public void setLearn(List<String> learn) {
        this.learn = learn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
