package com.skillswap.backend.controller;

import com.skillswap.backend.dto.SessionStatusDto;
import com.skillswap.backend.model.SkillSession;
import com.skillswap.backend.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for scheduling, completing, and cancelling skill sessions.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<SkillSession> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/student/{studentId}")
    public List<SkillSession> getSessionsForStudent(@PathVariable Long studentId) {
        return sessionService.getSessionsForStudent(studentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Long id) {
        try {
            SkillSession session = sessionService.getSessionById(id);
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SkillSession session) {
        try {
            SkillSession created = sessionService.createSession(session);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody SessionStatusDto statusDto) {
        try {
            if (statusDto == null || statusDto.getStatus() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Status field is required."));
            }
            SkillSession updated = sessionService.updateStatus(id, statusDto.getStatus());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long id) {
        try {
            SkillSession completed = sessionService.completeSession(id);
            return ResponseEntity.ok(completed);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id) {
        try {
            sessionService.deleteSession(id);
            return ResponseEntity.ok(Map.of("message", "Session deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
