package com.skillswap.backend.controller;

import com.skillswap.backend.dto.TransactionRequestDto;
import com.skillswap.backend.model.CoinTransaction;
import com.skillswap.backend.service.SkillCoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing SkillCoins balances and ledger transactions.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/skillcoins")
public class SkillCoinController {

    private final SkillCoinService skillCoinService;

    public SkillCoinController(SkillCoinService skillCoinService) {
        this.skillCoinService = skillCoinService;
    }

    @GetMapping("/{studentId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long studentId) {
        try {
            Integer balance = skillCoinService.getBalance(studentId);
            return ResponseEntity.ok(Map.of("studentId", studentId, "balance", balance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{studentId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long studentId) {
        try {
            List<CoinTransaction> list = skillCoinService.getTransactions(studentId);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{studentId}/transact")
    public ResponseEntity<?> transact(@PathVariable Long studentId, @RequestBody TransactionRequestDto req) {
        try {
            if (req == null || req.getAmount() == null || req.getAmount() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Transaction amount must be a positive integer."));
            }

            CoinTransaction tx;
            if ("DEBIT".equalsIgnoreCase(req.getType())) {
                tx = skillCoinService.debitCoins(studentId, req.getAmount(), req.getTitle() != null ? req.getTitle() : "Coin Debit");
            } else {
                tx = skillCoinService.creditCoins(studentId, req.getAmount(), req.getTitle() != null ? req.getTitle() : "Coin Credit");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(tx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
}
