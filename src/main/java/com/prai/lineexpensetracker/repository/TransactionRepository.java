package com.prai.lineexpensetracker.repository;

import com.prai.lineexpensetracker.entity.Transaction;
import com.prai.lineexpensetracker.enums.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUserLineUserIdOrderByCreatedAtDesc(String lineUserId);

    List<Transaction> findByUserLineUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            String lineUserId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Transaction> findByUserLineUserIdAndTypeAndTransactionDateBetween(
            String lineUserId,
            TypeTransaction type,
            LocalDate startDate,
            LocalDate endDate
    );

}
