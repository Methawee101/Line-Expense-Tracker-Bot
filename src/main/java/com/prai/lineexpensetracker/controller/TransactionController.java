package com.prai.lineexpensetracker.controller;

import com.prai.lineexpensetracker.dto.request.transactionRequest;
import com.prai.lineexpensetracker.dto.response.monthlySummaryResponse;
import com.prai.lineexpensetracker.dto.response.transactionResponse;
import com.prai.lineexpensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public transactionResponse createTransaction(@Valid @RequestBody transactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping
    public List<transactionResponse> getTransactionByLineUserId(@RequestParam String lineUserId) {
        return transactionService.getTransactionByLineUserId(lineUserId);
    }

    @GetMapping("/summary")
    public monthlySummaryResponse getMonthlySummary(
            @RequestParam String lineUserId,
            @RequestParam String month
    ){
        return  transactionService.getMonthlySummary(lineUserId, YearMonth.parse(month));
    }
}
