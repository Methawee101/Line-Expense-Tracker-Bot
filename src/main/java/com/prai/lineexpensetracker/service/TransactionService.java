package com.prai.lineexpensetracker.service;

import com.prai.lineexpensetracker.dto.request.transactionRequest;
import com.prai.lineexpensetracker.dto.response.transactionResponse;
import com.prai.lineexpensetracker.entity.Transaction;
import com.prai.lineexpensetracker.entity.User;
import com.prai.lineexpensetracker.enums.UserStatus;
import com.prai.lineexpensetracker.repository.TransactionRepository;
import com.prai.lineexpensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public transactionResponse createTransaction(transactionRequest request) {
        User user = userRepository.findByLineUserId(request.getLineUserId())
                .orElseGet(() -> createNewUser(request));

        Transaction transaction = Transaction.builder()
                .user(user)
                .type(request.getType())
                .amount(request.getAmount())
                .build();
        Transaction saveTransaction = transactionRepository.save(transaction);

        return toResponse(saveTransaction);
    }

    private User createNewUser(transactionRequest request) {
        User user = User.builder()
                .lineUserId(request.getLineUserId())
                .displayName(request.getDisplayName())
                .status(UserStatus.ACTIVE)
                .connectedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    private  transactionResponse toResponse(Transaction transaction) {
        User user = transaction.getUser();

        return transactionResponse.builder()
                .id(transaction.getId())
                .userId(user.getId())
                .lineUserId(user.getLineUserId())
                .DisplayName(user.getDisplayName())
                .type(transaction.getType())
                .title(transaction.getTitle())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
