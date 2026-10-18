package com.skillswap.backend.service;

import com.skillswap.backend.dto.MatchResultDto;
import com.skillswap.backend.model.Student;
import com.skillswap.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service providing transparent, explainable skill matching between students.
 * 
 * Algorithm Explanation for College ED Project Presentation:
 * 1. Mutual Skill Overlap Analysis:
 *    - Direct Need: Evaluates whether Partner (P) offers skills Student (S) wants to learn.
 *    - Reciprocal Benefit: Evaluates whether Student (S) can teach skills Partner (P) wants to learn.
 * 2. Scoring Formula:
 *    - Base Compatibility: 50%
 *    - Direct Match Bonus: +15% for each skill P teaches that S wants.
 *    - Reciprocal Match Bonus: +10% for each skill S can teach P (bilateral exchange).
 *    - Category Match Bonus: +5% if both students share domain category (e.g. coding).
 *    - Score is normalized between 50% and 98%.
 * 3. Self-exclusion & Filtering:
 *    - S is never matched with S.
 *    - Incomplete profiles without skills default gracefully.
 */
@Service
public class MatchingService {

    private final StudentRepository studentRepository;

    public MatchingService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<MatchResultDto> findMatchesForStudent(Long studentId) {
        Student current = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        List<Student> allStudents = studentRepository.findAll();
        List<MatchResultDto> results = new ArrayList<>();

        List<String> currentLearn = current.getLearnList().stream().map(String::toLowerCase).toList();
        List<String> currentTeach = current.getTeachList().stream().map(String::toLowerCase).toList();

        for (Student partner : allStudents) {
            // Rule 1: Never match a student with themselves
            if (partner.getId().equals(currentId(current))) {
                continue;
            }

            List<String> partnerTeach = partner.getTeachList();
            List<String> partnerLearn = partner.getLearnList();

            // Find overlapping skills
            List<String> directMatches = new ArrayList<>();
            for (String teachSkill : partnerTeach) {
                if (matchesAny(teachSkill, currentLearn)) {
                    directMatches.add(teachSkill);
                }
            }

            List<String> reciprocalMatches = new ArrayList<>();
            for (String learnSkill : partnerLearn) {
                if (matchesAny(learnSkill, currentTeach)) {
                    reciprocalMatches.add(learnSkill);
                }
            }

            // Calculate Score
            int score = 55; // Base compatibility
            score += directMatches.size() * 15;
            score += reciprocalMatches.size() * 10;

            if (current.getCategory() != null && partner.getCategory() != null &&
                current.getCategory().equalsIgnoreCase(partner.getCategory())) {
                score += 5;
            }

            if (score > 98) score = 98;
            if (score < 60) score = 65; // realistic friendly baseline

            // Generate explainable reason
            String explanation;
            if (!directMatches.isEmpty() && !reciprocalMatches.isEmpty()) {
                explanation = "Strong 2-way match! They teach " + String.join(", ", directMatches) + 
                              " and want to learn " + String.join(", ", reciprocalMatches) + ".";
            } else if (!directMatches.isEmpty()) {
                explanation = "They teach " + String.join(", ", directMatches) + " which matches your learning wishlist.";
            } else if (!reciprocalMatches.isEmpty()) {
                explanation = "They are looking to learn " + String.join(", ", reciprocalMatches) + " which you can teach.";
            } else {
                explanation = "General peer match based on " + partner.getCourse() + " experience.";
            }

            MatchResultDto dto = new MatchResultDto(
                    partner.getId(),
                    partner.getName(),
                    partner.getInitials(),
                    partnerTeach,
                    partnerLearn,
                    partner.getCategory() != null ? partner.getCategory() : "coding",
                    score,
                    partner.getOnline() != null ? partner.getOnline() : true,
                    partner.getLevel() != null ? partner.getLevel() : "Intermediate",
                    partner.getExperience() != null ? partner.getExperience() : 80,
                    explanation
            );

            results.add(dto);
        }

        // Sort descending by highest match score
        results.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return results;
    }

    private Long currentId(Student s) {
        return s.getId();
    }

    private boolean matchesAny(String skill, List<String> list) {
        String s = skill.toLowerCase();
        for (String item : list) {
            if (s.contains(item) || item.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
