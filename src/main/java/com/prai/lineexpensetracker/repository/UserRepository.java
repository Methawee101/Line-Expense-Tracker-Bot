package com.prai.lineexpensetracker.repository;

import com.prai.lineexpensetracker.entity.User;
import com.prai.lineexpensetracker.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLineUserId(String lineUserId);
    List<User> findByStatus(UserStatus status);
}