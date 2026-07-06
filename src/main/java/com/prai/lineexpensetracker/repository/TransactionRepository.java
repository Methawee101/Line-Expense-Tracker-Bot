package com.prai.lineexpensetracker.repository;

import com.prai.lineexpensetracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
