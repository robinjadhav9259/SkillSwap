package com.skillswap.backend.repository;

import com.skillswap.backend.model.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {

    List<CoinTransaction> findByStudentIdOrderByTimestampDesc(Long studentId);
}
