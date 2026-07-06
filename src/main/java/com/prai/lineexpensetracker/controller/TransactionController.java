package com.prai.lineexpensetracker.controller;

import com.prai.lineexpensetracker.dto.request.transactionRequest;
import com.prai.lineexpensetracker.dto.response.transactionResponse;
import com.prai.lineexpensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public transactionResponse createTransaction(@Valid @RequestBody transactionRequest request) {
        return transactionService.createTransaction(request);
    }
}
