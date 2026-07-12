package com.prai.lineexpensetracker.controller;

import com.prai.lineexpensetracker.dto.request.transactionRequest;
import com.prai.lineexpensetracker.dto.response.monthlySummaryResponse;
import com.prai.lineexpensetracker.dto.response.transactionResponse;
import com.prai.lineexpensetracker.service.ExcelExportService;
import com.prai.lineexpensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ExcelExportService excelExportService;

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

    @GetMapping("export")
    public ResponseEntity<byte[]> exportMonthlyTransactions(
            @RequestParam String lineUserId,
            @RequestParam String month

    ){
        YearMonth yearMonth = YearMonth.parse(month);

        byte[] excelFile = excelExportService.exportMonthlyTransactions(lineUserId,yearMonth);

        String fileName = "expense-report-" + month + ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename =" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }

}
