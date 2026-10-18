package com.skillswap.backend.controller;

import com.skillswap.backend.dto.MatchResultDto;
import com.skillswap.backend.service.MatchingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for skill matching calculations.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/matches")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getMatches(@PathVariable Long studentId) {
        try {
            List<MatchResultDto> matches = matchingService.findMatchesForStudent(studentId);
            return ResponseEntity.ok(matches);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
