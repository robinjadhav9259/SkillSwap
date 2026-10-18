package com.skillswap.backend.service;

import com.skillswap.backend.model.CoinTransaction;
import com.skillswap.backend.model.Student;
import com.skillswap.backend.repository.CoinTransactionRepository;
import com.skillswap.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing the SkillCoins virtual currency ledger.
 * Enforces transactional consistency, positive balance constraints, and transaction history audit.
 */
@Service
@Transactional
public class SkillCoinService {

    private final StudentRepository studentRepository;
    private final CoinTransactionRepository coinTransactionRepository;

    public SkillCoinService(StudentRepository studentRepository, 
                            CoinTransactionRepository coinTransactionRepository) {
        this.studentRepository = studentRepository;
        this.coinTransactionRepository = coinTransactionRepository;
    }

    @Transactional(readOnly = true)
    public Integer getBalance(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        return student.getCoins();
    }

    @Transactional(readOnly = true)
    public List<CoinTransaction> getTransactions(Long studentId) {
        return coinTransactionRepository.findByStudentIdOrderByTimestampDesc(studentId);
    }

    /**
     * Credit coins to a student's balance (e.g. after completing a teaching session).
     */
    public CoinTransaction creditCoins(Long studentId, int amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Credit amount must be strictly positive.");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        int newBalance = (student.getCoins() != null ? student.getCoins() : 0) + amount;
        student.setCoins(newBalance);
        studentRepository.save(student);

        CoinTransaction tx = new CoinTransaction(studentId, description, amount, "CREDIT", "Just now");
        return coinTransactionRepository.save(tx);
    }

    /**
     * Debit coins from a student's balance (e.g. booking a premium learning session).
     * Strictly prevents negative balances.
     */
    public CoinTransaction debitCoins(Long studentId, int amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Debit amount must be strictly positive.");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        int currentBalance = (student.getCoins() != null ? student.getCoins() : 0);
        if (currentBalance < amount) {
            throw new IllegalStateException("Insufficient SkillCoins. Current balance is " + 
                                           currentBalance + ", requested debit is " + amount + ".");
        }

        student.setCoins(currentBalance - amount);
        studentRepository.save(student);

        CoinTransaction tx = new CoinTransaction(studentId, description, amount, "DEBIT", "Just now");
        return coinTransactionRepository.save(tx);
    }
}
