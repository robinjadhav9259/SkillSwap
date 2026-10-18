package com.skillswap.backend.service;

import com.skillswap.backend.model.SkillSession;
import com.skillswap.backend.model.Student;
import com.skillswap.backend.repository.SkillSessionRepository;
import com.skillswap.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing skill exchange sessions (creation, retrieval, status transition, cancellation).
 */
@Service
@Transactional
public class SessionService {

    private final SkillSessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final SkillCoinService skillCoinService;

    public SessionService(SkillSessionRepository sessionRepository, 
                          StudentRepository studentRepository, 
                          SkillCoinService skillCoinService) {
        this.sessionRepository = sessionRepository;
        this.studentRepository = studentRepository;
        this.skillCoinService = skillCoinService;
    }

    public List<SkillSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<SkillSession> getSessionsForStudent(Long studentId) {
        return sessionRepository.findByStudentIdOrPartnerIdOrderBySessionDateAsc(studentId, studentId);
    }

    public SkillSession getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + id));
    }

    public SkillSession createSession(SkillSession session) {
        // Validation
        if (session.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID is required.");
        }
        if (session.getPartnerId() == null) {
            throw new IllegalArgumentException("Partner ID is required.");
        }
        if (session.getTopic() == null || session.getTopic().trim().isEmpty()) {
            throw new IllegalArgumentException("Topic / Skill is required.");
        }
        if (session.getSessionDate() == null || session.getSessionDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Session date is required.");
        }
        if (session.getStartTime() == null || session.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }

        // Fetch partner details if not populated
        if (session.getPartnerName() == null || session.getPartnerName().trim().isEmpty()) {
            Student partner = studentRepository.findById(session.getPartnerId()).orElse(null);
            if (partner != null) {
                session.setPartnerName(partner.getName());
                session.setPartnerInitials(partner.getInitials());
            } else {
                session.setPartnerName("Skill Partner #" + session.getPartnerId());
                session.setPartnerInitials("SP");
            }
        }

        if (session.getCoins() == null || session.getCoins() <= 0) {
            int duration = session.getDurationMinutes() != null ? session.getDurationMinutes() : 60;
            session.setCoins((int) Math.round((duration / 60.0) * 10));
        }

        session.setStatus("SCHEDULED");
        return sessionRepository.save(session);
    }

    public SkillSession updateStatus(Long id, String newStatus) {
        SkillSession session = getSessionById(id);

        if ("COMPLETED".equalsIgnoreCase(newStatus)) {
            return completeSession(id);
        } else if ("CANCELLED".equalsIgnoreCase(newStatus)) {
            session.setStatus("CANCELLED");
            return sessionRepository.save(session);
        } else {
            session.setStatus(newStatus.toUpperCase());
            return sessionRepository.save(session);
        }
    }

    public SkillSession completeSession(Long id) {
        SkillSession session = getSessionById(id);

        if ("COMPLETED".equalsIgnoreCase(session.getStatus())) {
            throw new IllegalStateException("Session is already marked as completed.");
        }

        session.setStatus("COMPLETED");
        sessionRepository.save(session);

        // Update student metrics
        Student student = studentRepository.findById(session.getStudentId()).orElse(null);
        if (student != null) {
            double addHours = session.getDurationMinutes() != null ? (session.getDurationMinutes() / 60.0) : 1.0;
            student.setHours(Math.round(((student.getHours() != null ? student.getHours() : 0.0) + addHours) * 10.0) / 10.0);
            student.setSessions((student.getSessions() != null ? student.getSessions() : 0) + 1);
            studentRepository.save(student);

            // Reward SkillCoins & record transaction
            int earnedCoins = session.getCoins() != null ? session.getCoins() : 10;
            String desc = "Completed " + session.getTopic() + " with " + session.getPartnerName();
            skillCoinService.creditCoins(session.getStudentId(), earnedCoins, desc);
        }

        return session;
    }

    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new IllegalArgumentException("Session not found with ID: " + id);
        }
        sessionRepository.deleteById(id);
    }
}
