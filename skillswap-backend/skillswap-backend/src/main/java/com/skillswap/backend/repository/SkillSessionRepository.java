package com.skillswap.backend.repository;

import com.skillswap.backend.model.SkillSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillSessionRepository extends JpaRepository<SkillSession, Long> {

    List<SkillSession> findByStudentIdOrPartnerIdOrderBySessionDateAsc(Long studentId, Long partnerId);

    List<SkillSession> findByStudentIdOrderBySessionDateAsc(Long studentId);
}
