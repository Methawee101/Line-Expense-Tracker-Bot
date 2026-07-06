package com.prai.lineexpensetracker.repository;

import com.prai.lineexpensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLineUserId(String lineUserId);
}