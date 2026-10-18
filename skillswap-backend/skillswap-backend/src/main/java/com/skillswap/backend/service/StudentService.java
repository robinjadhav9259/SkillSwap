package com.skillswap.backend.service;

import com.skillswap.backend.model.Student;
import com.skillswap.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing student profiles, registration, and updates.
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).orElse(null);
    }

    public Student createStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name is required.");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Student email is required.");
        }
        if (studentRepository.existsByEmail(student.getEmail().trim())) {
            throw new IllegalArgumentException("A student with email '" + student.getEmail() + "' already exists.");
        }

        student.setName(student.getName().trim());
        student.setEmail(student.getEmail().trim().toLowerCase());

        if (student.getCoins() == null) student.setCoins(760);
        if (student.getHours() == null) student.setHours(0.0);
        if (student.getSessions() == null) student.setSessions(0);
        if (student.getStreak() == null) student.setStreak(1);
        if (student.getOnline() == null) student.setOnline(true);
        if (student.getLevel() == null) student.setLevel("Intermediate");

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updated) {
        Student existing = getStudentById(id);

        if (updated.getName() != null && !updated.getName().trim().isEmpty()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getEmail() != null && !updated.getEmail().trim().isEmpty()) {
            String newEmail = updated.getEmail().trim().toLowerCase();
            if (!newEmail.equalsIgnoreCase(existing.getEmail()) && studentRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Email already in use by another student: " + newEmail);
            }
            existing.setEmail(newEmail);
        }
        if (updated.getCourse() != null) existing.setCourse(updated.getCourse());
        if (updated.getYear() != null) existing.setYear(updated.getYear());
        if (updated.getLevel() != null) existing.setLevel(updated.getLevel());
        if (updated.getBio() != null) existing.setBio(updated.getBio());
        if (updated.getTeachSkills() != null) existing.setTeachSkills(updated.getTeachSkills());
        if (updated.getLearnSkills() != null) existing.setLearnSkills(updated.getLearnSkills());
        if (updated.getCategory() != null) existing.setCategory(updated.getCategory());
        if (updated.getExperience() != null) existing.setExperience(updated.getExperience());

        return studentRepository.save(existing);
    }

    public Student getDefaultStudent() {
        return studentRepository.findAll().stream().findFirst()
                .orElse(null);
    }
}
