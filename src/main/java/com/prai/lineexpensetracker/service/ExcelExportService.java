package com.prai.lineexpensetracker.service;

import com.prai.lineexpensetracker.dto.response.transactionResponse;
import com.prai.lineexpensetracker.entity.Transaction;
import com.prai.lineexpensetracker.enums.TypeTransaction;
import com.prai.lineexpensetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {
    private final TransactionRepository transactionRepository;

    public byte[] exportMonthlyTransactions(String lineUserId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        List<Transaction> transactions =
                transactionRepository.findByUserLineUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                        lineUserId,
                        startDate,
                        endDate
                );

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Transactions");

            createHeaderRow(sheet);
            fillTransactionRows(sheet, transactions);
            createSummaryRows(sheet, transactions);

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel file", e);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("วันที่");
        headerRow.createCell(1).setCellValue("ประเภท");
        headerRow.createCell(2).setCellValue("รายการ");
        headerRow.createCell(3).setCellValue("จำนวนเงิน");
    }

    private void fillTransactionRows(Sheet sheet, List<Transaction> transactions) {
        int rowIndex = 1;

        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(transaction.getTransactionDate().toString());
            row.createCell(1).setCellValue(toThaiType(transaction.getType()));
            row.createCell(2).setCellValue(transaction.getTitle());
            row.createCell(3).setCellValue(transaction.getAmount().doubleValue());
        }
    }

    private void createSummaryRows(Sheet sheet, List<Transaction> transactions) {
        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> transaction.getType() == TypeTransaction.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(transaction -> transaction.getType() == TypeTransaction.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        int summaryStartRow = transactions.size() + 3;

        Row incomeRow = sheet.createRow(summaryStartRow);
        incomeRow.createCell(2).setCellValue("รายรับรวม");
        incomeRow.createCell(3).setCellValue(totalIncome.doubleValue());

        Row expenseRow = sheet.createRow(summaryStartRow + 1);
        expenseRow.createCell(2).setCellValue("รายจ่ายรวม");
        expenseRow.createCell(3).setCellValue(totalExpense.doubleValue());

        Row balanceRow = sheet.createRow(summaryStartRow + 2);
        balanceRow.createCell(2).setCellValue("คงเหลือ");
        balanceRow.createCell(3).setCellValue(balance.doubleValue());
    }

    private String toThaiType(TypeTransaction type) {
        if (type == TypeTransaction.INCOME) {
            return "รายรับ";
        }

        if (type == TypeTransaction.EXPENSE) {
            return "รายจ่าย";
        }

        return "-";
    }

}
